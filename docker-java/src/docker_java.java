//usr/bin/env jbang "$0" "$@" ; exit $?
//DEPS com.github.docker-java:docker-java:3.1.5
//DEPS javax.activation:activation:1.1.1
//DEPS org.slf4j:slf4j-simple:1.7.25

import com.github.dockerjava.api.model.BuildResponseItem;
import com.github.dockerjava.core.DockerClientBuilder;
import com.github.dockerjava.core.command.BuildImageResultCallback;

import java.io.File;
import java.util.Set;

public class docker_java
{
    public static void main(String[] args)
    {
        final var dockerClient = DockerClientBuilder.getInstance().build();

        BuildImageResultCallback callback = new BuildImageResultCallback()
        {
            @Override
            public void onNext(BuildResponseItem item)
            {
                System.out.print(item.getStream());
                super.onNext(item);
            }
        };

        dockerClient
            .buildImageCmd(new File("/Users/g/1/mandrel-packaging/docker"))
            .withTags(Set.of("mandrel-productization-builder:latest"))
            .exec(callback).awaitImageId();
    }
}
