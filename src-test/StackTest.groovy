import groovy.transform.TypeChecked

@TypeChecked
class StackTest extends GroovyTestCase {
    void testStack() {
        def stack = new Stack<Integer>()
        stack.add(1)
        stack.push(2)
        stack.push(3)
        println "stack = $stack"

        def list = new ArrayList<Integer>(stack)
        println "{list[0]} = ${list[0]}"

        def deque = new ArrayDeque<Integer>();
        deque.push(1)
        deque.push(2)
        deque.push(3)
        println "deque = $deque"
    }
}
