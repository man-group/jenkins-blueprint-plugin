package org.jenkinsci.plugins.yamlproject;

import hudson.Launcher;
import hudson.Extension;
import hudson.FilePath;
import hudson.util.FormValidation;
import hudson.model.AbstractBuild;
import hudson.model.BuildListener;
import hudson.model.AbstractProject;
import hudson.remoting.VirtualChannel;
import hudson.tasks.Builder;
import hudson.tasks.BuildStepDescriptor;
import net.sf.json.JSONObject;

import org.kohsuke.stapler.DataBoundConstructor;
import org.kohsuke.stapler.StaplerRequest;
import org.kohsuke.stapler.QueryParameter;
import org.yaml.snakeyaml.Yaml;

import javax.servlet.ServletException;

import java.io.File;
import java.io.IOException;
import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

/**
 * Build a project according to .jenkins.yml.
 *
 * <p>
 * When the user configures the project and enables this builder,
 * {@link DescriptorImpl#newInstance(StaplerRequest)} is invoked
 * and a new {@link YamlBuilder} is created. The created
 * instance is persisted to the project configuration XML by using
 * XStream, so this allows you to use instance fields (like {@link #name})
 * to remember the configuration.
 *
 * <p>
 * When a build is performed, the {@link #perform(AbstractBuild, Launcher, BuildListener)}
 * method will be invoked. 
 *
 */
public class YamlBuilder extends Builder {

    // Fields in config.jelly must match the parameter names in the "DataBoundConstructor"
    @DataBoundConstructor
    public YamlBuilder() {
    }

    @Override
    public boolean perform(AbstractBuild build, Launcher launcher, BuildListener listener) throws IOException, InterruptedException {
        // Fetch the YAML file, then build accordingly.

        final FilePath workspace = build.getWorkspace();
        final FilePath ymlPath = workspace.child(".jenkins.yml");

        PrintStream logger = listener.getLogger();
        if (!ymlPath.exists()) {
            logger.println("No .jenkins.yml found in" + workspace);
            return false;
        }

        String ymlSrc = ymlPath.act(new YamlFileFetcher());

        Yaml yaml = new Yaml();
        Map<String, List<String>> yamlComponents;
        try {
            yamlComponents = (Map<String, List<String>>) yaml.load(ymlSrc);
        } catch (ClassCastException e) {
            logger.println("Your .jenkins.yml is invalid, it should have a top-level 'script:'.");
            return false;
        }

        logger.println("Parsed YAML:");
        logger.println(yamlComponents);

        return true;
    }

    // Overridden for better type safety.
    // If your plugin doesn't really define any property on Descriptor,
    // you don't have to do this.
    @Override
    public DescriptorImpl getDescriptor() {
        return (DescriptorImpl)super.getDescriptor();
    }

    /**
     * Descriptor for {@link YamlBuilder}. Used as a singleton.
     * The class is marked as public so that it can be accessed from views.
     *
     * <p>
     * See <tt>src/main/resources/hudson/plugins/hello_world/YamlBuilder/*.jelly</tt>
     * for the actual HTML fragment for the configuration screen.
     */
    @Extension // This indicates to Jenkins that this is an implementation of an extension point.
    public static final class DescriptorImpl extends BuildStepDescriptor<Builder> {
        /**
         * In order to load the persisted global configuration, you have to 
         * call load() in the constructor.
         */
        public DescriptorImpl() {
            load();
        }

        public boolean isApplicable(Class<? extends AbstractProject> aClass) {
            // Indicates that this builder can be used with all kinds of project types 
            return true;
        }

        /**
         * This human readable name is used in the configuration screen.
         */
        public String getDisplayName() {
            return "Build according to .jenkins.yml";
        }
    }
}

class YamlFileFetcher implements FilePath.FileCallable<String> {
    public String invoke(File ws, VirtualChannel channel) throws FileNotFoundException {
        return new Scanner(ws).useDelimiter("\\Z").next();
    }
}
