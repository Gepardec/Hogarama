package org.dcm4che.test.support;

import java.io.Serializable;

/**
 * Metadata containing additional information about what should be done before test call
 */
public class WarpMeta implements Serializable {
    boolean executeBeforeWarp;

    public boolean isExecuteInTransaction() {
        return executeInTransaction;
    }

    public void setExecuteInTransaction(boolean executeInTransaction) {
        this.executeInTransaction = executeInTransaction;
    }

    boolean executeInTransaction;

    public boolean isExecuteBeforeWarp() {
        return executeBeforeWarp;
    }

    public void setExecuteBeforeWarp(boolean executeBeforeWarp) {
        this.executeBeforeWarp = executeBeforeWarp;
    }
}
