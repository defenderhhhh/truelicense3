/*
 * Copyright (C) 2005-2015 Schlichtherle IT Services.
 * All rights reserved. Use is subject to license terms.
 */

package org.truelicense.swing;

import java.util.concurrent.ExecutionException;
import javax.swing.AbstractButton;
import javax.swing.ButtonModel;
import javax.swing.SwingUtilities;
import javax.swing.SwingWorker;
import static org.truelicense.ui.LicenseWizardMessage.welcome_display;
import static org.truelicense.ui.LicenseWizardMessage.welcome_install;
import static org.truelicense.ui.LicenseWizardMessage.welcome_prompt;
import static org.truelicense.ui.LicenseWizardMessage.welcome_uninstall;
import org.truelicense.ui.LicenseWizardState;

/**
 * @author Christian Schlichtherle
 */
final class WelcomePanel extends LicensePanel {

    private static final long serialVersionUID = 1L;

    WelcomePanel(final LicenseManagementWizard wizard) {
        super(wizard);
        initComponents();
        uninstallButton.setVisible(false);
    }

    /**
     * Returns {@code true} if and only if the uninstall-button is visible.
     * By default, this button is <em>not</em> visible.
     */
    public boolean isUninstallButtonVisible() {
        return uninstallButton.isVisible();
    }

    /** Sets the visibility of the uninstall-button. */
    public void setUninstallButtonVisible(final boolean visible) {
        uninstallButton.setVisible(visible);
        validate();
    }

    /**
     * Starts a background task to check if a license key is installed and
     * updates the radio buttons accordingly.
     */
    @Override public void onAfterStateSwitch() {
        assert SwingUtilities.isEventDispatchThread();
        assert isVisible();
        wizard().disableNextButton();
        displayButton.setEnabled(false);
        uninstallButton.setEnabled(false);
        new SwingWorker<Boolean, Void>() {
            @Override protected Boolean doInBackground() {
                return licenseInstalled();
            }

            @Override protected void done() {
                assert SwingUtilities.isEventDispatchThread();
                boolean installed;
                try {
                    installed = get(); // try in worker thread
                } catch (InterruptedException ex) {
                    installed = licenseInstalled(); // do in event dispatcher thread
                } catch (ExecutionException ex) {
                    throw new AssertionError();
                }
                displayButton.setEnabled(installed);
                uninstallButton.setEnabled(installed);
                buttonGroup.setSelected(defaultSelection().getModel(), true);
                wizard().enableNextButton();
            }
        }.execute();
    }

    private AbstractButton defaultSelection() {
        switch (super.nextState()) {
            case install: return installButton;
            case display: return displayButton;
            default: throw new AssertionError();
        }
    }

    @Override public LicenseWizardState nextState() {
        return LicenseWizardState.valueOf(actionCommand());
    }

    private String actionCommand() { return selection().getActionCommand(); }

    private ButtonModel selection() { return buttonGroup.getSelection(); }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        javax.swing.JTextArea prompt = new javax.swing.JTextArea();
        installButton = new org.truelicense.swing.util.EnhancedRadioButton();
        displayButton = new org.truelicense.swing.util.EnhancedRadioButton();
        uninstallButton = new org.truelicense.swing.util.EnhancedRadioButton();

        setName(WelcomePanel.class.getSimpleName());
        setLayout(new java.awt.GridBagLayout());

        prompt.setEditable(false);
        prompt.setFont(getFont());
        prompt.setLineWrap(true);
        prompt.setText(format(welcome_prompt)); // NOI18N
        prompt.setWrapStyleWord(true);
        prompt.setBorder(null);
        prompt.setName(welcome_prompt.name());
        prompt.setOpaque(false);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = java.awt.GridBagConstraints.REMAINDER;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.ipady = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.SOUTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 15, 0);
        add(prompt, gridBagConstraints);

        buttonGroup.add(installButton);
        installButton.setSelected(true);
        installButton.setText(format(welcome_install)); // NOI18N
        installButton.setActionCommand(LicenseWizardState.install.name());
        installButton.setName(welcome_install.name());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        add(installButton, gridBagConstraints);

        buttonGroup.add(displayButton);
        displayButton.setText(format(welcome_display)); // NOI18N
        displayButton.setActionCommand(LicenseWizardState.display.name());
        displayButton.setName(welcome_display.name());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        add(displayButton, gridBagConstraints);

        buttonGroup.add(uninstallButton);
        uninstallButton.setText(format(welcome_uninstall)); // NOI18N
        uninstallButton.setActionCommand(LicenseWizardState.uninstall.name());
        uninstallButton.setName(welcome_uninstall.name());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        add(uninstallButton, gridBagConstraints);
    }// </editor-fold>//GEN-END:initComponents

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private final javax.swing.ButtonGroup buttonGroup = new javax.swing.ButtonGroup();
    private org.truelicense.swing.util.EnhancedRadioButton displayButton;
    private org.truelicense.swing.util.EnhancedRadioButton installButton;
    private org.truelicense.swing.util.EnhancedRadioButton uninstallButton;
    // End of variables declaration//GEN-END:variables
}
