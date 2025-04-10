package com.thetinkeringtypist.pentastick.instanceloader;

import java.util.Objects;
import java.util.Optional;
import java.util.ServiceLoader;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 * A wrapper around the Java service loader that supports Singletons.
 *
 * @apiNote According to the Java Service Loader, we should not cache objects globally when loaded
 *  by the service loader. Different threads can have different context class loaders and so those class loaders
 *  may not have access to objects loaded by other threads, which may violate the intended use of such an object.
 */
public class InstanceLoader {
    // The map of classes for loaded instances
    private static final ConcurrentHashMap<Class<? extends InstanceLoadable>, Singleton> SINGLETONS = new ConcurrentHashMap<>();


    private InstanceLoader(){
        // Non-instantiable constructor
    }


    /**
     * Clear all internally cached singletons and service loaders
     */
    public static void clear() {
        SINGLETONS.clear();
    }


    /**
     * Create and return an instance of the provided service interface if one exists. If this returned instance
     * implements the Singleton interface, then each subsequent call to this method will return the same instance.
     *
     * @param serviceInterface the interface type of the requested service.
     * @return an Optional containing an instance of the requested service. This Optional is empty if no instance exists.
     */
    public static <S extends InstanceLoadable> Optional<S> load(final Class<? extends InstanceLoadable> serviceInterface) {
        return load(serviceInterface, "");
    }


    /**
     * Create and return an instance of the provided service interface with the provided implementation if one exists.
     * If this returned instance implements the Singleton interface, then each subsequent call to this method will
     * return the same instance for the provided service interface regardless of implementation.
     *
     * @param serviceInterface the interface type of the requested service.
     * @param implementation the binary name of the implementation to load, if possible.
     * @return an Optional containing an instance of the requested service. This Optional is empty if no instance exists.
     */
    public static <S extends InstanceLoadable> Optional<S> load(final Class<? extends InstanceLoadable> serviceInterface,
                                                                final String implementation) {
        // If class is not specified, return an empty optional
        if (Objects.isNull(serviceInterface) || Objects.isNull(implementation)) {
            return Optional.empty();
        }

        // Singleton has already been loaded, return it
        InstanceLoadable instance = SINGLETONS.get(serviceInterface);
        if (Objects.nonNull(instance)) {
            return Optional.of((S) instance);
        }

        ServiceLoader<? extends InstanceLoadable> loader = ServiceLoader.load(serviceInterface);
        Optional<? extends InstanceLoadable> result;

        // Nothing loaded yet, and we don't care which one we load
        if (implementation.isEmpty()) {
            result = loader.findFirst();
        } else {
            // Looking for a particular implementation to load
            result = loader.stream()
                    .filter(p -> Objects.equals(p.type().getName(), implementation))
                    .map(ServiceLoader.Provider::get)
                    .findFirst();
        }

        // If result is present AND is a singleton, add it to the singleton map
        if (result.isPresent() && (result.get() instanceof Singleton singleton)) {
            SINGLETONS.put(serviceInterface, singleton);
        }

        // Return our optional containing the newly loaded instance
        return (Optional<S>) result;
    }


    /**
     * Return a set of all instances of the provided service interface. The returned set is unmodifiable.
     *
     * @param serviceInterface the interface type of the requested service.
     */
    public static <S extends InstanceLoadable> Set<S> loadAll(final Class<? extends InstanceLoadable> serviceInterface) {
        Set<? extends InstanceLoadable> resultSet = Set.of();
        if (Objects.isNull(serviceInterface)) {
            return (Set<S>) resultSet;
        }

        // Singleton, return a collection containing only the singleton
        if (Singleton.class.isAssignableFrom(serviceInterface)) {
            Optional<? extends InstanceLoadable> result = InstanceLoader.load(serviceInterface);
            if (result.isPresent()) {
                resultSet = Set.of(result.get());
            }
        } else {
            ServiceLoader<? extends InstanceLoadable> loader = ServiceLoader.load(serviceInterface);
            resultSet = loader.stream()
                    .map(ServiceLoader.Provider::get)
                    .collect(Collectors.toUnmodifiableSet());
        }

        return (Set<S>) resultSet;
    }


    /**
     * Get the set of available implementations for the given service interface. The returned set is a list of binary
     * names for the available services. The returned set is unmodifiable. If not implementations are available, the
     * returned set is empty.
     *
     * @param serviceInterface the interface type of the requested service.
     * @return an unmodifiable list of binary names of the available services for the provided service interface.
     */
    public static Set<String> getAvailableServices(final Class<? extends InstanceLoadable> serviceInterface) {
        if (Objects.isNull(serviceInterface)) {
            return Set.of();
        }

        ServiceLoader<? extends InstanceLoadable> loader = ServiceLoader.load(serviceInterface);

        // Return an unmodifiable list of service names
        return loader.stream()
                .map(p -> p.type().getName())
                .collect(Collectors.toUnmodifiableSet());
    }
}
