package cloud.martinodutto.wtsapi.configuration;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public interface ConfigurationParameters {

    @Nonnull
    String getSchTasksCommand();

    @Nullable
    String getRemoteSystem();

    @Nullable
    String getRemoteUser();

    @Nullable
    String getRemotePassword();
}
