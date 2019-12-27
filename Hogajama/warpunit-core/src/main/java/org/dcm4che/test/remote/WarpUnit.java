/*
 *
 * ** BEGIN LICENSE BLOCK *****
 * Version: MPL 1.1/GPL 2.0/LGPL 2.1
 *
 * The contents of this file are subject to the Mozilla Public License Version
 * 1.1 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 * http://www.mozilla.org/MPL/
 *
 * Software distributed under the License is distributed on an "AS IS" basis,
 * WITHOUT WARRANTY OF ANY KIND, either express or implied. See the License
 * for the specific language governing rights and limitations under the
 * License.
 *
 * The Initial Developer of the Original Code is
 * Agfa Healthcare.
 * Portions created by the Initial Developer are Copyright (C) 2016
 * the Initial Developer. All Rights Reserved.
 *
 * Contributor(s):
 * See @authors listed below
 *
 * Alternatively, the contents of this file may be used under the terms of
 * either the GNU General Public License Version 2 or later (the "GPL"), or
 * the GNU Lesser General Public License Version 2.1 or later (the "LGPL"),
 * in which case the provisions of the GPL or the LGPL are applicable instead
 * of those above. If you wish to allow use of your version of this file only
 * under the terms of either the GPL or the LGPL, and not to allow others to
 * use your version of this file under the terms of the MPL, indicate your
 * decision by deleting the provisions above and replace them with the notice
 * and other provisions required by the GPL or the LGPL. If you do not delete
 * the provisions above, a recipient may use your version of this file under
 * the terms of any one of the MPL, the GPL or the LGPL.
 *
 * **** END LICENSE BLOCK *****
 *
 */

package org.dcm4che.test.remote;

import org.dcm4che.test.support.WarpMeta;
import org.jboss.resteasy.client.jaxrs.ResteasyWebTarget;
import sun.reflect.ConstantPool;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.FutureTask;
import java.util.function.Supplier;

/**
 * @author rawmahn
 */
public class WarpUnit {

    public static String DEFAULT_REMOTE_ENDPOINT_URL = makeURL("localhost", "8080");

    private static Executor executor;

    private static Executor getExecutor() {

        if (executor != null)
            return executor;

        synchronized (WarpUnit.class) {
            if (executor == null)
                executor = Executors.newCachedThreadPool();
        }

        return executor;
    }


    public static String makeURL(String host, String port) {
        return "http://" + host + ":" + port + "/warpunit-insider";
    }

    /**
     * Use this builder to create a warp gate.
     */
    public static GateBuilder builder() {
        return new GateBuilder();
    }

    /**
     * Use {@link #builder} instead
     */
    @Deprecated
    public static <T> T warp(final Class<T> insiderInterface, final Class<? extends T> insiderClass, final boolean warpInterface, final String url) {
        return makeProxyGate(insiderInterface, warpInterface, url, insiderClass);
    }

    @Deprecated
    /**
     * Use {@link #builder} instead
     */
    public static <T> T warp(final Class<T> insiderInterface, final Class<? extends T> insiderClass) {
        return warp(insiderInterface, insiderClass, false, null);
    }


    public static WarpGate createGate(String url, Class ... classes) {
        return new WarpGate0(url, classes);
    }


    public static WarpGate createGate(Class ... classes) {
        return new WarpGate0(WarpUnit.DEFAULT_REMOTE_ENDPOINT_URL, classes);
    }

    static <T> T makeProxyGate(final Class<T> insiderInterface, final boolean warpInterface, final String url, final Class... classes) {

        Class[] classesToSend;
        if (warpInterface) {
            classesToSend = Arrays.copyOf(classes, classes.length + 1);
            classesToSend[classesToSend.length - 1] = insiderInterface;
        } else {
            classesToSend = classes;
        }

        Object proxy = Proxy.newProxyInstance(insiderInterface.getClassLoader(), new Class[]{insiderInterface}, new InvocationHandler() {
            @Override
            public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                return warp(null, method.getName(), args, classesToSend, url);
            }
        });
        return (T) proxy;
    }

    public static class WarpGate0 implements WarpGate {

        private Class[] classes;
        private String url;

        public WarpGate0(String url, Class... classes) {
            this.classes = classes;
            this.url = url;
        }

        @Override
        public <R> R warp(Supplier<R> lambda) {
            return WarpUnit.warp(null, lambda, classes, url);
        }

        @Override
        public <R> R warp(WarpMeta meta, Supplier<R> lambda) {
            return WarpUnit.warp(meta, lambda, classes, url);
        }

        @Override
        public void warp(Runnable lambda) {
            WarpUnit.warp(null, lambda, classes, url);
        }

        @Override
        public void warp(WarpMeta meta, Runnable lambda) {
            WarpUnit.warp(meta, lambda, classes, url);
        }

        @Override
        public <R> Future<R> warpAsync(Supplier<R> lambda) {
            return warpAndMakeFuture(null,lambda);
        }

        @Override
        public <R> Future<R> warpAsync(WarpMeta meta, Supplier<R> lambda) {
            return warpAndMakeFuture(meta, lambda);
        }

        @Override
        public Future<Void> warpAsync(Runnable lambda) {
            return warpAndMakeFuture(null, lambda);
        }

        @Override
        public Future<Void> warpAsync(WarpMeta meta, Runnable lambda) {
            return warpAndMakeFuture(null, lambda);
        }

        @Override
        public Object warp(String methodName, Object[] args) {
            return WarpUnit.warp(null, methodName, args, classes, url);
        }

        @Override
        public Object warp(WarpMeta meta, String methodName, Object[] args) {
            return WarpUnit.warp(meta, methodName, args, classes, url);
        }

        private <R> Future<R> warpAndMakeFuture(WarpMeta meta, Object warpable) {
            FutureTask futureTask = new FutureTask<>(() -> WarpUnit.warp(meta, warpable, classes, url));
            getExecutor().execute(futureTask);
            return futureTask;
        }


    };

    /**
     * Not sure if useful, will keep for now
     */
    private static class WarpGateMockRunsLocally implements WarpGate {

        @Override
        public <R> R warp(Supplier<R> lambda) {
            return lambda.get();
        }

        @Override
        public <R> R warp(WarpMeta meta, Supplier<R> lambda) {
            return lambda.get();
        }

        @Override
        public void warp(Runnable lambda) {
            lambda.run();
        }

        @Override
        public void warp(WarpMeta meta, Runnable lambda) {
            lambda.run();
        }

        @Override
        public <R> Future<R> warpAsync(Supplier<R> lambda) {
            FutureTask<R> futureTask = new FutureTask<>(() -> lambda.get());
            getExecutor().execute(futureTask);
            return futureTask;
        }

        @Override
        public <R> Future<R> warpAsync(WarpMeta meta, Supplier<R> lambda) {
            FutureTask<R> futureTask = new FutureTask<>(() -> lambda.get());
            getExecutor().execute(futureTask);
            return futureTask;
        }

        @Override
        public Future<Void> warpAsync(Runnable lambda) {
            FutureTask<Void> futureTask = new FutureTask<>(() -> {
                lambda.run();
                return null;
            });
            getExecutor().execute(futureTask);
            return futureTask;
        }

        @Override
        public Future<Void> warpAsync(WarpMeta meta, Runnable lambda) {
            FutureTask<Void> futureTask = new FutureTask<>(() -> {
                lambda.run();
                return null;
            });
            getExecutor().execute(futureTask);
            return futureTask;
        }

        @Override
        public Object warp(String methodName, Object[] args) {
            throw new RuntimeException("not implemented");
        }

        @Override
        public Object warp(WarpMeta meta, String methodName, Object[] args) {
            throw new RuntimeException("not implemented");
        }

    }

    private static <T> T warp(WarpMeta meta, Object lambda, Class[] classes, String url) {

        try {
            List<Object> args = new ArrayList<>();
            // figure out closure-parameters
            for (Field field : lambda.getClass().getDeclaredFields()) {
                field.setAccessible(true);

                Object o = field.get(lambda);

                // if it's the primary(i.e. enclosing for this lambda) class then it's not interesting
                if (classes[0].equals(o.getClass())) continue;

                args.add(o);
            }

            // get lambda's actual method name in parent class
            Method getConstantPool = Class.class.getDeclaredMethod("getConstantPool");
            getConstantPool.setAccessible(true);
            ConstantPool constantPool = (ConstantPool) getConstantPool.invoke(lambda.getClass());

            String[] methodRefInfo;

            // hmm gotta look how to make it stable
            try {
                methodRefInfo = constantPool.getMemberRefInfoAt(constantPool.getSize() - 2);
            } catch (IllegalArgumentException e) {
                methodRefInfo = constantPool.getMemberRefInfoAt(constantPool.getSize() - 3);
            }


            return (T) warp(meta, methodRefInfo[1], args.toArray(), classes, url);
        } catch (Exception e) {
            throw new RuntimeException("Warp failed", e);
        }
    }

    /**
     * @param classes classes to warp. The [0]th class is considered primary - i.e. the once whose method will be called
     */
    private static Object warp(WarpMeta meta, String methodName, Object[] args, Class[] classes, String url) throws RemoteExecutionException {
        RemoteRequestJSON requestJSON = new RemoteRequestJSON();

        requestJSON.methodName = methodName;
        requestJSON.primaryClassName = classes[0].getName();
        requestJSON.args = Base64.toBase64(DeSerializer.serialize(args));
        requestJSON.metadata = Base64.toBase64(DeSerializer.serialize(meta));
        requestJSON.classes = new HashMap<>();


        for (Class aClass : classes) {

            String insiderClassResourceName = getClassResourceName(aClass);
            URL insiderClassResource = aClass.getResource(insiderClassResourceName);
            requestJSON.classes.put(aClass.getName(), Base64.toBase64(getBytes(insiderClassResource)));

            // inner classes
            for (Class<?> innerClass : aClass.getDeclaredClasses()) {

                String[] splitClassName = innerClass.getName().split("\\.");
                String classFileName = splitClassName[splitClassName.length - 1] + ".class";
                URL resource = innerClass.getResource(classFileName);
                requestJSON.classes.put(innerClass.getName(), Base64.toBase64(getBytes(resource)));
            }
        }

        String base64resp = getRemoteEndpoint(url).warpAndRun(requestJSON);

        Object returned;
        try {
            returned = DeSerializer.deserialize(Base64.fromBase64(base64resp));
        } catch (IOException e) {
            throw new RuntimeException("Error while deserializing the result of remotely executed code");
        }

        if (returned instanceof RemoteExecutionException)
            throw (RemoteExecutionException) returned;

        if (returned instanceof Exception)
            throw new RemoteExecutionException("Unexpected error while running code remotely", (Throwable) returned);

        return returned;
    }


    private static String getClassResourceName(Class<?> clazz) {
        StringBuffer classResourceName = new StringBuffer();
        Class<?> declaringClass = clazz.getDeclaringClass();
        if (declaringClass != null) {
            classResourceName.append(declaringClass.getSimpleName()).append("$");
        }

        classResourceName.append(clazz.getSimpleName()).append(".class");

        return classResourceName.toString();
    }

    private static byte[] getBytes(URL resource) {
        try {
            URLConnection connection = resource.openConnection();
            InputStream input = connection.getInputStream();
            ByteArrayOutputStream buffer = new ByteArrayOutputStream();
            int data = input.read();

            while (data != -1) {
                buffer.write(data);
                data = input.read();
            }

            input.close();

            return buffer.toByteArray();
        } catch (IOException e) {
            throw new RuntimeException("Error retrieving bytecode for resource "+resource, e);
        }
    }

    private static WarpUnitInsiderREST getRemoteEndpoint(String url) {
        // create jax-rs client
        Client client = ClientBuilder.newBuilder().build();

        String remoteEndpointUrl;
        if (url != null) {
            remoteEndpointUrl = url;
        } else {
            remoteEndpointUrl = DEFAULT_REMOTE_ENDPOINT_URL;
        }

        WebTarget target = client.target(remoteEndpointUrl);
        ResteasyWebTarget rtarget = (ResteasyWebTarget) target;
        return rtarget.proxy(WarpUnitInsiderREST.class);
    }


}
