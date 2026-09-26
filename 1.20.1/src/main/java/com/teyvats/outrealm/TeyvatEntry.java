package com.teyvats.outrealm;

import java.util.function.Supplier;

/**
 * Fabric 移植用的惰性注册条目。
 *
 * 替代 NeoForge 的 DeferredRegister/DeferredHolder：
 * 字段在类加载时仅记录工厂，实际对象在 onInitialize 注册阶段
 * 首次调用 {@link #get()} 时才创建。这样字段声明顺序与交叉引用
 * 均保持与原版（NeoForge）一致，其余代码中的 .get() 调用无需改动。
 */
public final class TeyvatEntry<T> implements Supplier<T> {
    private final Supplier<T> factory;
    private T value;

    public TeyvatEntry(Supplier<T> factory) {
        this.factory = factory;
    }

    @Override
    public T get() {
        if (value == null) {
            value = factory.get();
        }
        return value;
    }
}
