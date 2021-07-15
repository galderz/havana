package docker.java;

import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.api.command.AttachContainerCmd;
import com.github.dockerjava.api.command.CreateContainerCmd;
import com.github.dockerjava.api.command.CreateContainerResponse;
import com.github.dockerjava.api.command.RemoveContainerCmd;
import com.github.dockerjava.api.command.StartContainerCmd;
import com.github.dockerjava.core.DefaultDockerClientConfig;
import com.github.dockerjava.core.DockerClientBuilder;
import com.github.dockerjava.core.DockerClientConfig;
import com.github.dockerjava.core.command.AttachContainerResultCallback;
import com.github.dockerjava.netty.NettyDockerCmdExecFactory;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;

// copied from https://github.com/docker-java/docker-java/issues/941#issuecomment-346377948
// run with: -Dorg.slf4j.simpleLogger.defaultLogLevel=debug
public class DockerStdin
{
    public static void main(String[] args)
    {
        DockerClientConfig config = DefaultDockerClientConfig.createDefaultConfigBuilder().build();

        DockerClient docker = DockerClientBuilder.getInstance(config).withDockerCmdExecFactory(new NettyDockerCmdExecFactory())
            .build();

        // final String image = "ubuntu:16.04";
        final String image = "fedora:31";

        CreateContainerCmd createContainerCmd = docker
            .createContainerCmd(image)
            .withCmd("/bin/sh")
            .withStdinOpen(true)
            .withTty(false);
        CreateContainerResponse createContainerResponse = createContainerCmd.exec();

        StartContainerCmd startContainerCmd = docker.startContainerCmd(createContainerResponse.getId());
        startContainerCmd.exec();

        try (PipedOutputStream out = new PipedOutputStream();
             PipedInputStream in = new PipedInputStream(out);
             BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(out));
             BufferedReader reader = new BufferedReader(new InputStreamReader(System.in))) {
            AttachContainerCmd attachContainerCmd = docker.attachContainerCmd(createContainerResponse.getId()).withStdIn(in)
                .withStdOut(true).withStdErr(true).withFollowStream(true);
            attachContainerCmd.exec(new AttachContainerResultCallback());

            String line = "Hello World!";
            while (!"q".equals(line)) {
                writer.write(line + "\n");

                writer.flush();

                line = reader.readLine();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        RemoveContainerCmd removeContainerCmd = docker.removeContainerCmd(createContainerResponse.getId()).withRemoveVolumes(true)
            .withForce(true);
        removeContainerCmd.exec();
    }

}
