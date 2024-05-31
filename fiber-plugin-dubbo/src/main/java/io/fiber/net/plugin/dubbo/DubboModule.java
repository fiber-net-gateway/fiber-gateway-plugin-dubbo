package io.fiber.net.plugin.dubbo;

import io.fiber.net.common.ioc.Binder;
import io.fiber.net.common.ioc.Module;
import io.fiber.net.common.utils.StringUtils;
import io.fiber.net.common.utils.SystemPropertyUtil;
import io.fiber.net.proxy.HttpLibConfigure;
import io.fiber.net.proxy.ProxyModule;

public class DubboModule implements Module {
    private static DubboConfig defaultConfig() {
        String registry = SystemPropertyUtil.get("fiber.dubbo.registry");
        if (StringUtils.isEmpty(registry)) {
            throw new IllegalStateException("no dubbo registry configured. -Dfiber.dubbo.registry=");
        }
        DubboConfig config = new DubboConfig();
        config.setRegistryAddr(registry);
        config.setApplicationName(SystemPropertyUtil.get("fiber.dubbo.appName", config.getApplicationName()));
        config.setProtocol(SystemPropertyUtil.get("fiber.dubbo.protocol", config.getProtocol()));
        return config;
    }

    @Override
    public void install(Binder binder) {
        binder.bindFactory(DubboClient.class, injector -> new DubboClient(defaultConfig()));
        binder.bind(SubModule.class, new SubModule());
        binder.bindMultiBean(ProxyModule.class, SubModule.class);
    }

    private static class SubModule implements ProxyModule {

        @Override
        public void install(Binder binder) {
            binder.bindPrototype(DubboLibConfigure.class, DubboLibConfigure::new);
            binder.bindMultiBean(HttpLibConfigure.class, DubboLibConfigure.class);
            binder.bindFactory(DubboRefManager.class, DubboRefManager::new);
        }
    }
}
