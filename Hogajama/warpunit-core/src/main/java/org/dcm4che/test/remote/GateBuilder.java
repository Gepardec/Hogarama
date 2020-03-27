package org.dcm4che.test.remote;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class GateBuilder {


    private String url = WarpUnit.DEFAULT_REMOTE_ENDPOINT_URL;

    private boolean includeInterface;
    private List<Class> classes;


    public GateBuilder() {

        classes = new ArrayList<>();

        // for primary class
        classes.add(null);
    }

    /**
     * Optional. Overrides the RESTful Endpoint URL of warpunit insider which will be used to communicate with the server.
     * Default is {@link WarpUnit#DEFAULT_REMOTE_ENDPOINT_URL}
     */
    public GateBuilder url(String url) {
        this.url = url;
        return this;
    }

    /**
     * Primary class is the class whose lambdas (or methods in case of using the proxy syntax) will be called on the server.
     * It can be seen as an 'entry point' of the warp'd code that is ran on the server.
     * @param primaryClass The class
     */
    public GateBuilder primaryClass(Class primaryClass) {
        classes.set(0, primaryClass);
        return this;
    }

    public GateBuilder moreClasses(Class... moreClasses) {
        Collections.addAll(classes, moreClasses);
        return this;
    }


    /**
     * Only for proxy syntax. Specifies whether the interface class should be also sent over to the server. Default is false.
     * Should be set to {@code true} in case the proxy interface is not known on the server.
     */
    public GateBuilder includeInterface(boolean includeInterface) {
        this.includeInterface = includeInterface;
        return this;
    }

    /**
     * Creates a proxy of type {@code insiderInterface} which upon calling its methods will
     * warp the primary class and call the corresponding methods of primary class's instance on the server.
     * {@code primaryClass} must implement {@code insiderInterface}
     * @param insiderInterface interface of the proxy
     * @return A proxy-style warp gate.
     */
    public <T> T createProxyGate(Class<T> insiderInterface) {

        if (insiderInterface == null)
            throw new IllegalArgumentException("insiderInterface cannot be null");

        if (classes.get(0) == null)
            throw new IllegalArgumentException("Primary class not specified");

        return WarpUnit.makeProxyGate(insiderInterface, includeInterface, url, makeArrayOfClasses());
    }

    private Class[] makeArrayOfClasses() {
        return classes.toArray(new Class[classes.size()]);
    }


    /**
     * Creates a lambda-style gate.
     * The lambdas that are warp'd through this gate must be defined in the primary class.
     * This gate also includes non-type-safe reflection-style methods.
     * @return A lambda-style warp gate
     */
    public WarpGate createGate() {
        return WarpUnit.createGate(url, makeArrayOfClasses());
    }



}
