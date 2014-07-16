import groovy.transform.CompileStatic
import groovy.transform.TypeChecked
import org.junit.Test

@TypeChecked
class GroovyNullTest {
    static interface Map2 extends Map {}

    static int method(Object arg) { return 1; }
    static int method(String arg) { return 2; }

    @Test
    public void test() {
//        System.out.println(method(null));

        Map2 map = null
//        Map<?, ?> map = null
//        if (map == null) println "map is null"
    }
}
