import org.junit.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.*;
import java.util.stream.Collectors;

/**
 * java8 lambda使用指南
 *
 * @author liuguanshen
 * @create 2023/6/16
 * @description
 */
@SpringBootTest
public class LambdaLabs {

    /**
     * lambda进行遍历
     */
    @Test
    public void lambda4IterationCollection() {

        List<String> list = Arrays.asList("test1", "test2", "test3");

        //老遍历方式
        for (String s : list) {
            System.out.println(s);
        }

        System.out.println("-----------分隔符-------------");

        //lambda的forEach，这里的s是自定义变量
        list.forEach(s -> {
            System.out.println(s);
        });

        System.out.println("-----------分隔符-------------");

        //lambda的forEach，极简模式，这里的::是java8的另一个特性"方法引用"
        list.forEach(System.out::println);
    }

    /**
     * lambda进行排序
     */
    @Test
    public void lambda4Sort() {

        //老方式排序
        List<String> list = Arrays.asList("2", "1", "4", "3");
        Collections.sort(list, new Comparator<String>() {
            @Override
            public int compare(String o1, String o2) {
                return o1.compareTo(o2);
            }
        });
        System.out.println(list);

        System.out.println("-----------分隔符-------------");

        //lambda方式排序
        List<String> list2 = Arrays.asList("2", "1", "5", "3", "4");
        //下面几种方法效果一致
        {
            /*Collections.sort(list2, (o1, o2) -> {
                return o1.compareTo(o2);
            });*/

            Collections.sort(list2, (o1, o2) -> o1.compareTo(o2));

            //Collections.sort(list, String::compareTo);
        }
        //上面几种方法效果一致
        System.out.println(list2);

        System.out.println("-----------分隔符-------------");
    }


    /**
     * lambda进行过滤
     */
    @Test
    public void lambda4Filter() {

        List<String> list = Arrays.asList("apple", "and", "to", "test");

        //老方式过滤，首字母包含a的
        List<String> list2 = new ArrayList<>();
        for (String s : list) {
            if (s.startsWith("a")) {
                list2.add(s);
            }
        }
        System.out.println(list2);

        System.out.println("-----------分隔符-------------");

        //lambda方式过滤，首字母包含a的
        List<String> aStartList = list.stream().filter(s -> s.startsWith("t")).collect(Collectors.toList());
        System.out.println(aStartList);

        System.out.println("-----------分隔符-------------");
    }

    /**
     * lambda进行映射
     */
    @Test
    public void lambda4Map() {
        //需求：去除list中每一个元素的长度
        List<String> list = Arrays.asList("apple", "and", "to", "test");
        List<Integer> lengthList = new ArrayList<>();

        //老方法
        for (String s : list) {
            lengthList.add(s.length());
        }
        System.out.println(lengthList);

        System.out.println("-----------分隔符-------------");

        /*map的作用，可以看作是对管道流中的每一个数据，进行转化处理
        这里就是对s这个string类型，转化成了s.length()的返回值int类型*/
        List<Integer> lengthList2 = list.stream().map(s -> s.length()).collect(Collectors.toList());
        System.out.println(lengthList2);

        System.out.println("-----------分隔符-------------");
    }


    /**
     * lambda进行归约
     */
    @Test
    public void lambda4Reduction() {

        List<Integer> list = Arrays.asList(2, 1, 5, 3, 4);

        //老方法
        int sum = 0;
        for (Integer v : list) {
            sum += v;
        }
        System.out.println(sum);

        System.out.println("-----------分隔符-------------");

        //lambda方式
        /*0=identity=初始值
        第一次计算的时候，0会给a赋值，b就是管道流获得的list中的元素值，然后a+b的值又会返回给identity也就是0
        第二次的时候，a这个值就是上一次a+b的返回值了*/
        Integer reduce = list.stream().reduce(0, (a, b) -> a + b);
        System.out.println(reduce);

        System.out.println("-----------分隔符-------------");

    }


    /**
     * lambda进行分组
     */
    @Test
    public void lambda4Group() {

        //按string的长度分组，key=长度，value=符合长度的string
        List<String> list = Arrays.asList(
                "a",
                "to", "ya",
                "and", "for", "man",
                "test", "word", "hash");

        //老方法
        HashMap<Integer, List<String>> groups = new HashMap<>();
        for (String s : list) {
            if (!groups.containsKey(s.length())) {
                //如果map中，不包含s长度的键值对，则创建键值对，并在value中的list添加元素
                groups.put(s.length(), new ArrayList<>());
                groups.get(s.length()).add(s);
            } else {
                List<String> listElement = groups.get(s.length());
                listElement.add(s);
            }
        }
        System.out.println(groups);

        System.out.println("-----------分隔符-------------");

        //lambda方式
        {
            //下面方法等价
            //注意这里使用的"Collectors方法"这是Stream流的方法，注意引用
            Map<Integer, List<String>> collect = list.stream().collect(Collectors.groupingBy(s -> s.length()));
            Map<Integer, List<String>> collect1 = list.stream().collect(Collectors.groupingBy(String::length));
            System.out.println(collect);
            System.out.println(collect1);
        }


        System.out.println("-----------分隔符-------------");

    }


    /**
     * lambda实现函数式接口
     */
    @Test
    public void lambda4FunctionalInterface() {

        //老方式，实现函数式接口
        MyInterface myInterface = new MyInterface() {
            @Override
            public void doSomething(String s) {
                System.out.println(s);
            }
        };
        myInterface.doSomething("老方式，实现函数式接口~");

        System.out.println("-----------分隔符-------------");

        //lambda，实现函数式接口
        MyInterface myInterface1 = (s) -> System.out.println(s);
        myInterface1.doSomething("lambda，实现函数式接口~");

        System.out.println("-----------分隔符-------------");

    }

    public interface MyInterface {
        public void doSomething(String S);
    }


    /**
     * lambda创建线程
     */
    @Test
    public void lambda4Thread() {
        //老方式
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                System.out.println("老方式，创建线程");
            }
        });
        thread.start();

        System.out.println("-----------分隔符-------------");

        //lambda
        Thread thread1 = new Thread(() -> System.out.println("lambda，创建线程"));
        thread1.start();


        System.out.println("-----------分隔符-------------");
    }


    /**
     * lambda进行Option操作
     * <p>
     * option也是java8新特性，判空null操作，可以避免大部分空指针
     * 注意这是判空，空串不属于null
     */
    @Test
    public void lambda4Option() {

        String s = "hello word";

        //老方式
        if (null != s) {
            System.out.println(s.toUpperCase());
        }

        System.out.println("-----------分隔符-------------");

        //lambda
        {
            //下面方法等价
            Optional.ofNullable(s).map(String::toUpperCase).ifPresent(System.out::println);
            //这里的s=s1=s2，因为变量名重复的关系，就改了名字
            Optional.ofNullable(s).map(s1 -> s1.toUpperCase()).ifPresent(s2 -> System.out.println(s2));
        }

        System.out.println("-----------分隔符-------------");

    }


    /**
     * lambda进行Stream流水线操作
     */
    @Test
    public void lambda4Stream() {
        List<String> list = Arrays.asList(
                "a",
                "am", "ya",
                "and", "for", "man",
                "also", "word", "hash");
        List<String> list2 = new ArrayList<>();

        //老方法。针对流水线肯定需要foreach处理
        //假如这里的需求：查出list中的所有a开头的元素，并且全部转大写，然后排序输出
        for (String s : list) {
            if (s.startsWith("a")){
                list2.add(s.toUpperCase());
            }
        }
        Collections.sort(list2, new Comparator<String>() {
            @Override
            public int compare(String o1, String o2) {
                return o1.compareTo(o2);
            }
        });
        System.out.println(list2);

        System.out.println("-----------分隔符-------------");

        List<String> list3 = list.stream()
                .filter(s -> s.startsWith("a"))
                .map(s -> s.toUpperCase())
                .collect(Collectors.toList());
        Collections.sort(list3,((o1, o2) -> o1.compareTo(o2)));
        System.out.println(list3);

        System.out.println("-----------分隔符-------------");

        List<String> list4 = list.stream()
                .filter(s -> s.startsWith("a"))
                .map(s -> s.toUpperCase())
                //.sorted((o1, o2) -> o1.compareTo(o2)) //这种默认的可以不用写排序逻辑,直接sorted()即可
                .sorted()
                .collect(Collectors.toList());
        System.out.println("流水线-正向排序 = "+ list4);

        System.out.println("-----------分隔符-------------");

        List<String> list5 = list.stream()
                .filter(s -> s.startsWith("a"))
                .map(s -> s.toUpperCase())
                .sorted((o1, o2) -> o2.compareTo(o1)) //这种默认的可以不用写排序逻辑
                .collect(Collectors.toList());
        System.out.println("流水线-反向排序 = "+ list5);
    }

}
