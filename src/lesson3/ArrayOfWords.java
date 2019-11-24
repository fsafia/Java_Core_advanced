package lesson3;

import java.util.HashMap;
import java.util.Map;

public class ArrayOfWords {

    private String [] animals1;

    ArrayOfWords(String... animals1){
        this.animals1 = animals1;
    }

    public Map<String, Integer> wordCount(){
        Map<String,Integer> map = new HashMap<>();
        for(int i = 0; i < animals1.length; i++){
            String key = animals1[i].toLowerCase();
            Integer count = map.get(key);
            map.put(key, count = (count == null)? 1 : count + 1);
        }

        System.out.println(map);
        return map;

    }


}
