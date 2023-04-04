import java.util.ArrayList;
import java.util.LinkedList;
import java.util.stream.IntStream;

import static java.lang.Math.sqrt;

public class HashMap <T1 extends Comparable<T1>, T2> implements Comparable<T1> {
    private ArrayList<LinkedList<node<T1, T2>>> head = new ArrayList<>(); // массив со списками с парами ключ-значение
    int m, allNumb=0; // число списков и пар соотвественно
    double a = 0.0; // уровень загруженности

    public HashMap(int m) {                 // конструктор
        if (isPrime(m)) {
            this.m = m;
            for (int i=0; i<m; ++i)
                head.add(null);
        } else
            System.out.println("Вы ввели недопустимый модуль!");
    }
    private boolean isPrime(int number) {           // проверка на простоту
        return number > 1
                && IntStream.rangeClosed(2, (int) sqrt(number))
                .noneMatch(n -> (number % n == 0));
    }

    public void AddEl(Object key, T2 data) {     // добавление пары ключ-значение
        int heshKey = CountHeshKey(key, this.m);

        if (heshKey > -1 && heshKey < m) {
            ++allNumb;
            a = (double) allNumb / m;
            if (a <= 2)
                head.set(heshKey, AddDelStroc(head.get(heshKey),(T1) key, data, true));
            else {
                Rehashing();
                a = (double) allNumb / m;
                heshKey = CountHeshKey(key, this.m);
                head.set(heshKey, AddDelStroc(head.get(heshKey),(T1) key, data, true));
            }
        } else
            System.out.println("Выявлена проблема с ключом " + key + "! Добаление элемента не произошло.");
    }
    private int CountHeshKey(Object key, int mod){                 // расчет по ключу любого типа целочисленного хеш-ключа
        int heshKey=-5;
        if (key instanceof String) {
            String[] tmpMas = ((String) key).split("");
            long tmpkey = 0;
            for (int i = 0; i < tmpMas.length; ++i)
                tmpkey = tmpkey * 10 + TranslateToInt(tmpMas[i]);
            heshKey = (int) (tmpkey % mod);
        } else
            heshKey = (int) key % mod;
        return heshKey;
    }
    private LinkedList<node<T1, T2>> AddDelStroc(LinkedList<node<T1, T2>> orig, T1 key, T2 data, boolean marker) {  // добавление/удаление пары ключ-значение из списка по хеш-ключу
        if (marker) {
            if (orig == null) {
                LinkedList<node<T1, T2>> adding = new LinkedList<>();
                adding.add(new node(key, data));
                return adding;
            } else {
                orig.add(new node(key, data));
                return orig;
            }
        }else{
            if (orig != null){
                for (int i=0; i<orig.size(); ++i)
                    if (key.compareTo(orig.get(i).key) == 0){
                        orig.remove(i);
                        break;
                    }
                return orig;
            }else
                System.out.println("В хеш-дереве нет значения по такому ключу.");
            return orig;
        }
    }
    private void Rehashing(){       // перехеширование хеш-таблицы
        int newM = this.m;
        boolean marker = true;
        do {
            int tmp = newM*2+1;
            if (isPrime(tmp)) {
                marker = false;
                newM = tmp;
            }else
                ++newM;
        }while (marker);

        ArrayList<LinkedList<node<T1, T2>>> tmpHead = new ArrayList<>();
        for (int i=0; i<newM; ++i)
            tmpHead.add(null);

        for (int i=0; i<m; ++i){
            int size = 0;
            if (head.get(i) != null)
                size = head.get(i).size();
            for (int j=0; j<size; ++j) {
                node tmpNode = head.get(i).get(j);
                int NewHeshKey = CountHeshKey(tmpNode.key, newM);
                //System.out.println(i+" -- "+j+" * "+tmpNode.key+" - "+NewHeshKey);
                tmpHead.set(NewHeshKey, AddDelStroc(tmpHead.get(NewHeshKey), (T1) tmpNode.key, (T2) tmpNode.data, true));
            }
        }

        this.head = tmpHead;
        this.m = newM;
    }

    public void DelAll(){          // очистка хеш-таблицы
        if (head == null || allNumb == 0)
            System.out.println("Таблица и так пуста. Остановись!");
        for (int i=0; i<head.size(); ++i)
            head.set(i, null);
        System.out.println("Хеш-таблица очищена!");
        a = 0.0;
        allNumb = 0;
    }
    public node GetPair(T1 key){                // метод, возвращающий пару: ключ-значение по ключу
        node answerNode = new node(null, null);
        int heshKey = CountHeshKey(key, this.m);
        int marker = 0, size = 0;
        if (head.get(heshKey) != null)
            size = head.get(heshKey).size();
        for (int j=0; j<size; ++j)
            if (key.compareTo(head.get(heshKey).get(j).key) == 0){
                marker = 1;
                answerNode = head.get(heshKey).get(j);
                break;
            }

        if (marker == 0)
            System.out.println("В хеш-дереве нет значения по такому ключу");
        return answerNode;
    }

    private int TranslateToInt(String orig) {       // перевод строки в число
        int answer = -5;
        char c = orig.charAt(0);
        answer = (int) c;
        return answer;
    }
    public void DelEl (T1 key){             // удаление пары по ключу
        if (head == null || allNumb == 0)
            System.out.println("Таблица и так пуста");
        else {
            int heshKey = CountHeshKey(key, this.m);
            int size = 0;
            boolean marker = true;
            if (head.get(heshKey) != null)
                size = head.get(heshKey).size();
            for (int j=0; j<size; ++j)
                if (key.compareTo(head.get(heshKey).get(j).key) == 0){
                    marker = false;
                    head.set(heshKey, AddDelStroc(head.get(heshKey), key, null, false));
                    System.out.println("Значение по ключу "+key+" удалено из таблицы.");
                    -- allNumb;
                    a = (double) allNumb/m;
                    break;
                }

            if (marker)
                System.out.println("В хеш-дереве нет значения по такому ключу");
        }
    }
    public void ReHashing(){        // перехеширование по желанию пользователя
        Rehashing();
    }
    public void print(){            // вывод хеш-таблицы
        System.out.println("/------------------------------|");
        for (int i=0; i<m; ++i) {
            System.out.print("По хешу "+i+" хранятся данные: ");
            int size = 0;
            if (head.get(i) != null)
                size = head.get(i).size();
            for (int j = 0; j < size; ++j) {
                System.out.print(head.get(i).get(j).key+" - "+head.get(i).get(j).data+"; ");
            }
            System.out.println("");
        }
        System.out.println("|------------------------------\\");
    }
    public void SizeAndСafficient(){            // вывод основных параметров
        System.out.println("Пар в хеш-таблице: "+allNumb);
        System.out.println("Число списков: "+m);
        System.out.println("Вещественный коэффициент: "+a);
    }

    @Override
    public int compareTo(T1 o) {
        return 0;
    }
}
