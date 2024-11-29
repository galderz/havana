package annprocess.example;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import java.util.Set;

// Use a different value to the other processors
@SupportedAnnotationTypes("*")
public class AnnProcessExample extends AbstractProcessor
{
    @Override
    public SourceVersion getSupportedSourceVersion()
    {
        return SourceVersion.latest();
    }

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv)
    {
        System.out.println("AnnProcessorExample.process");
        for (Element element : roundEnv.getRootElements())
        {
            if (element instanceof TypeElement)
            {
                TypeElement typeElement = (TypeElement) element;
                String qualifiedName = typeElement.getQualifiedName().toString();
                if (qualifiedName.contains("jmh_generated"))
                {
                    System.out.println("Found JMH generated class: " + qualifiedName);
                }
            }
        }
        return true;
    }
}
