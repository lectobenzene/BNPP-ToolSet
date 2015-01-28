package groovycode.mavenrepopusher.models

public class ParametersModel implements Serializable {
    private String groupId
    private String artifactId
    private String version
    private String packaging

    public ParametersModel() {
    }

    public String getGroupId() {
        return groupId
    }

    public void setGroupId(final String groupId) {
        this.groupId = groupId
    }

    public String getArtifactId() {
        return artifactId
    }

    public void setArtifactId(final String artifactId) {
        this.artifactId = artifactId
    }

    public String getVersion() {
        return version
    }

    public void setVersion(final String version) {
        this.version = version
    }

    public String getPackaging() {
        return packaging
    }

    public void setPackaging(final String packaging) {
        this.packaging = packaging
    }
}