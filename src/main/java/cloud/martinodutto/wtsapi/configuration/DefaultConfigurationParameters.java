package cloud.martinodutto.wtsapi.configuration;

public final class DefaultConfigurationParameters implements ConfigurationParameters {

    private static DefaultConfigurationParameters INSTANCE = new DefaultConfigurationParameters();

    private DefaultConfigurationParameters() {
    }

    public static DefaultConfigurationParameters getInstance() {
        return INSTANCE;
    }

    @Override
    public String getSchTasksCommand() {
        return "schtasks.exe";
    }
}
