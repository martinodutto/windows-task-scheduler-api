package cloud.martinodutto.wtsapi.configuration;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class LocalConfigurationParameters implements ConfigurationParameters {

    private static LocalConfigurationParameters INSTANCE = new LocalConfigurationParameters();

    private LocalConfigurationParameters() {
    }

    public static LocalConfigurationParameters getInstance() {
        return INSTANCE;
    }

    @Nonnull
    @Override
    public String getSchTasksCommand() {
        return "schtasks.exe";
    }

    @Nullable
    @Override
    public final String getRemoteSystem() {
        return null;
    }

    @Nullable
    @Override
    public final String getRemoteUser() {
        return null;
    }

    @Nullable
    @Override
    public final String getRemotePassword() {
        return null;
    }
}
