package com.goodworkalan.paste;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import com.google.inject.Key;
import com.google.inject.Provider;
import com.google.inject.Scope;

public class Scopes {
    /**
     * Scopes a provider in the the HTTP request session.
     */
    public final static Scope SESSION = new Scope() {
        /**
         * Scopes a provider in the the HTTP request session. The returned
         * provider returns objects from this scope. If an object does not exist
         * in this scope, the given unscoped provider is used to retrieve one.
         * 
         * @param key
         *            The binding key.
         * @param unscoped
         *            Use to locate an instance if one does not already exist in
         *            the scope.
         * @return A new provider which only delegates to the given unscoped
         *         provider when an instance of the requested object doesn't
         *         already exist in this scope.
         */
        public <T> Provider<T> scope(Key<T> key, final Provider<T> unscoped) {
            final String name = key.toString();
            return new Provider<T>() {
                public T get() {
                    HttpSession session = PasteGuicer.getRequestFiltration().getRequest().getSession();

                    @SuppressWarnings("unchecked")
                    T t = (T) session.getAttribute(name);
                    if (t == null) {
                        t = unscoped.get();
                        session.setAttribute(name, t);
                    }
                    return t;
                }
            };
        }
    };
    
    /**
     * Scopes a provider in the the HTTP request session.
     */
    public final static Scope REQUEST = new Scope() {
        /**
         * Scopes a provider in the the HTTP request session. The returned
         * provider returns objects from this scope. If an object does not exist
         * in this scope, the given unscoped provider is used to retrieve one.
         * 
         * @param key
         *            The binding key.
         * @param unscoped
         *            Use to locate an instance if one does not already exist in
         *            the scope.
         * @return A new provider which only delegates to the given unscoped
         *         provider when an instance of the requested object doesn't
         *         already exist in this scope.
         */
        public <T> Provider<T> scope(Key<T> key, final Provider<T> unscoped) {
            final String name = key.toString();
            return new Provider<T>() {
                public T get() {
                    HttpServletRequest request = PasteGuicer.getRequestFiltration().getRequest();

                    @SuppressWarnings("unchecked")
                    T t = (T) request.getAttribute(name);
                    if (t == null) {
                        t = unscoped.get();
                        request.setAttribute(name, t);
                    }
                    return t;
                }
            };
        }
    };
    
    public final static Scope FILTER = new Scope() {
        public <T> Provider<T> scope(final Key<T> key, final Provider<T> unscoped) {
            return new Provider<T>() {
                public T get() {
                    Map<Key<?>, Object> map = PasteGuicer.getFilterFiltration().getFilterScope();

                    @SuppressWarnings("unchecked")
                    T t = (T) map.get(key);
                    if (t == null) {
                        t = unscoped.get();
                        map.put(key, t);
                    }
                    return t;
                }
            };
        }
    };

    public final static Scope CONTROLLER = new Scope() {
        public <T> Provider<T> scope(final Key<T> key, final Provider<T> unscoped) {
            return new Provider<T>() {
                public T get() {
                    Map<Key<?>, Object> map = PasteGuicer.getFilterFiltration().getControllerScope();

                    @SuppressWarnings("unchecked")
                    T t = (T) map.get(key);
                    if (t == null) {
                        t = unscoped.get();
                        map.put(key, t);
                    }
                    return t;
                }
            };
        }
    };
}
