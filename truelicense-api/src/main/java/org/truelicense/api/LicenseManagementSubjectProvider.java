/*
 * Copyright (C) 2005-2015 Schlichtherle IT Services.
 * All rights reserved. Use is subject to license terms.
 */

package org.truelicense.api;

/**
 * Provides the license management subject.
 *
 * @author Christian Schlichtherle
 */
public interface LicenseManagementSubjectProvider {

    /** Returns the license management subject. */
    String subject();
}
