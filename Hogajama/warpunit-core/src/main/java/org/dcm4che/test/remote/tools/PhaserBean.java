
package org.dcm4che.test.remote.tools;

import java.util.concurrent.Phaser;

/**
 * An application scoped bean to enable synchronization between calls from different contexts
 *
 * @author rawmahn
 */
public interface PhaserBean {

    /**
     * @return current active Phaser
     */
    Phaser get();

    /**
     * Discards the existing Phaser and creates a new one which becomes the active one
     */
    void reset();

}
