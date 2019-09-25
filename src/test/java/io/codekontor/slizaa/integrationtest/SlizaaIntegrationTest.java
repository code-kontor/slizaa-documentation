package io.codekontor.slizaa.integrationtest;

import io.codekontor.slizaa.server.SlizaaServerConfiguration;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.MethodParameter;
import org.springframework.shell.MethodTarget;
import org.springframework.shell.Shell;
import org.springframework.shell.Utils;
import org.springframework.shell.jcommander.JCommanderParameterResolver;
import org.springframework.shell.jline.InteractiveShellApplicationRunner;
import org.springframework.shell.standard.StandardParameterResolver;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Map;
import java.util.stream.Stream;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(properties = {InteractiveShellApplicationRunner.SPRING_SHELL_INTERACTIVE_ENABLED + "=" + false, "graphql.servlet.websocket.enabled=false"})
@ContextConfiguration(classes = SlizaaServerConfiguration.class)
public class SlizaaIntegrationTest {

    @Autowired
    private Shell shell;

    @Autowired
    private StandardParameterResolver parameterResolver;

    @Test
    public void test() {

        System.out.println(shell);

        Map<String, MethodTarget> commandMap = shell.listCommands();

        commandMap.forEach((name, target) -> {
            System.out.println(name + " : " + target.getHelp());

			Stream<MethodParameter> methodParameter = Utils.createMethodParameters(target.getMethod());
            methodParameter.forEach(p -> parameterResolver.describe(p).forEach(d -> {
                System.out.println(d.keys());
            }));
        });
    }
}