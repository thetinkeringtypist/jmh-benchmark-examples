package com.thetinkeringtypist.pentastick.instanceloader;

/**
 * This interface is used by the {@link InstanceLoader} to follow the singleton
 * design pattern at runtime instead of compile time.
 *
 * @see <a href="https://en.wikipedia.org/wiki/Singleton_pattern">The Singleton Design Pattern</a>
 * @see com.thetinkeringtypist.pentastick.instanceloader.InstanceLoader
 */
public interface Singleton extends InstanceLoadable {}
