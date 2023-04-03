import static java.lang.Math.sqrt;

public class TestHashMap {
    public static void main(String[] args) {
        HashMap<Integer, String> test = new HashMap<>(5);
        test.AddEl(3, "Addel");
        test.AddEl(5, "Back");
        test.AddEl(1, "Lily");
        test.AddEl(9, "Janny");
        test.AddEl(8, "Sam");
        test.AddEl(2, "Madam");
        test.AddEl(6, "Jerry");
        test.AddEl(10, "John");
        test.AddEl(52, "Igor");
        test.AddEl(17, "Boby");
        test.AddEl(24, "Marta");

        test.print();
        test.SizeAndСafficient();
        System.out.println("-----------------------------------");
        node testNode = test.GetPair(1);
        if (testNode.key != null)
            System.out.println("В хеш-таблице по ключу "+testNode.key+" лежат данные: "+testNode.data);
        System.out.println("-----------------------------------");
        test.DelEl(52);
        test.GetPair(52);
        System.out.println("-----------------------------------");
        test.ReHashing();
        test.print();
        test.SizeAndСafficient();
    }
}
