package de.daver.unihub;

import de.daver.unihub.util.PrefixLogger;
import de.daver.unihub.util.StopWatch;
import lombok.Getter;
import org.bukkit.plugin.java.JavaPlugin;

public abstract class UniversePlugin extends JavaPlugin {

    @Getter
    private PrefixLogger pLogger;

    @Override
    public void onEnable() {
        pLogger = new PrefixLogger(this.getName());
        pLogger.info("Start initialising Plugin...");

        StopWatch watch = new StopWatch() {
            @Override
            public void run() {
                init();
                onInitialisation();
            }
        };
        watch.start();

        pLogger.info("Finished after " + watch.getMillis() + "ms");

    }

    @Override
    public void onDisable() {
        pLogger.info("Start terminating Plugin...");

        StopWatch watch = new StopWatch() {
            @Override
            public void run() {
                onTermination();
            }
        };

        watch.start();

        pLogger.info("Finished after " + watch.getMillis() + "ms");
    }

    private void init(){

    }

    protected abstract void onTermination();
    protected abstract void onInitialisation();
}
