package org.dcm4che.test.remote;

import java.util.concurrent.Future;
import java.util.function.Supplier;

/**
 * Lambda-style warp gate.
 */
public interface WarpGate {

    /**
     * Warps a lambda, i.e. runs it on the server and returns the result back.
     * @param lambda A lambda to warp. The lambda is allowed to have closures (which must be serializable).
     *               This lambda MUST be defined in the <i>primary class</i> defined for this gate.
     *
     *
     * @throws RemoteExecutionException in case an exception was thrown during invocation of the lambda on the server
     * @return Result of this lambda invocation on the server
     */
    <R> R warp(Supplier<R> lambda);

    /**
     * Same as {@link #warp}, but the specified lambda has no return value
     * @return
     */
    void warp(Runnable lambda);

    /**
     * Warps (see {@link #warp}) a lambda asynchronously.
     * @return A future that is resolved when the warp returns. The value returned by .get() is the same value that {@link #warp} method would return (or the same exception is thrown).
     */
    <R> Future<R> warpAsync(Supplier<R> lambda);

    /**
     * Same as {@link #warpAsync(Supplier)}, but the specified lambda has no return value
     */
    Future<Void> warpAsync(Runnable lambda);

    /**
     * Non-type-safe way to warp the specified method on the primary class defined for the gate.
     * Reflection-friendly.
     * @param methodName name of a method to execute
     * @param args arguments
     * @return result of a warp'd execution (on the server)
     */
    Object warp(String methodName, Object[] args);
}
