/*
 * Copyright (C) 2005-2015 Schlichtherle IT Services.
 * All rights reserved. Use is subject to license terms.
 */

package org.truelicense.v2.commons.crypto;

import org.truelicense.api.crypto.EncryptionParameters;
import org.truelicense.api.io.Sink;
import org.truelicense.api.io.Source;
import org.truelicense.api.passwd.PasswordUsage;
import org.truelicense.core.crypto.BasicEncryption;

import javax.crypto.Cipher;
import javax.crypto.CipherInputStream;
import javax.crypto.CipherOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.security.AlgorithmParameters;

import static javax.crypto.Cipher.*;

/**
 * The encryption for V2 format license keys.
 * This class is immutable.
 *
 * @author Christian Schlichtherle
 */
public final class V2Encryption extends BasicEncryption {

    public V2Encryption(EncryptionParameters parameters) { super(parameters); }

    @Override
    public Sink apply(final Sink sink) {
        return () -> wrap(() -> {
            final Cipher cipher = cipher(PasswordUsage.WRITE, null);
            final AlgorithmParameters
                    param = cipher.getParameters();
            final byte[] encoded = param.getEncoded();
            assert encoded.length <= Short.MAX_VALUE;
            final OutputStream out = sink.output();
            try {
                new DataOutputStream(out).writeShort(encoded.length);
                out.write(encoded);
            } catch (final Throwable t) {
                try { out.close(); }
                catch (Throwable t2) { t.addSuppressed(t2); }
                throw t;
            }
            return new CipherOutputStream(out, cipher);
        });
    }

    @Override
    public Source unapply(final Source source) {
        return () -> wrap(() -> {
            final Cipher cipher;
            final InputStream in = source.input();
            try {
                final DataInputStream din = new DataInputStream(in);
                final byte[] encoded = new byte[din.readShort() & 0xffff];
                din.readFully(encoded);
                cipher = cipher(PasswordUsage.READ, param(encoded));
            } catch (final Throwable t) {
                try { in.close(); }
                catch (Throwable t2) { t.addSuppressed(t2); }
                throw t;
            }
            return new CipherInputStream(in, cipher);
        });
    }

    private AlgorithmParameters param(final byte[] encoded) throws Exception {
        final AlgorithmParameters
                param = AlgorithmParameters.getInstance(algorithm());
        param.init(encoded);
        return param;
    }

    private Cipher cipher(final PasswordUsage usage, final AlgorithmParameters param) throws Exception {
        final Cipher cipher = getInstance(algorithm());
        cipher.init(
                PasswordUsage.WRITE.equals(usage) ? ENCRYPT_MODE : DECRYPT_MODE,
                secretKey(usage), param);
        return cipher;
    }
}
