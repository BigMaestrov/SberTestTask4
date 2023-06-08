package org.example;

import java.io.IOException;
import java.io.PrintStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class Main {
    public static void main(String[] args) {
        try {
            Scanner scanner = new Scanner(System.in);
            //Начало отсчета времени
            long time = System.currentTimeMillis();
            //Заполнения списка для вывода информации
            ArrayList<City> cityList = parseCSV("./src/main/resources/data.csv");
            //Конец отсчета времени
            System.out.println((System.currentTimeMillis() - time) + " мс" + ", Найдено строк: " + cityList.size());
            //Выбор сортировки
            System.out.print("Выберите тип операции" +
                    "\n" + "1.Сортировка по наименования города" +
                    "\n" + "2.Сортировка по федеральному округу и наименованию города" +
                    "\n" + "3.Поиск города с наибольшим населением" +
                    "\n" + "4.Поиск кол-ва городов в регионе" +
                    "\n" + ">>");
            String userEnter = scanner.nextLine();
            switch (userEnter) {
                case ("1"):
                    // Используем компаратор, игнорирующий регистр
                    Comparator<City> comparator = Comparator.comparing(City::getName, String.CASE_INSENSITIVE_ORDER);
                    // Сортируем список
                    Collections.sort(cityList, comparator);
                    //Вывод списка
                    for (City city : cityList) {
                        System.out.println(city.toString());
                    }
                    break;
                case ("2"):
                    // Создаем компаратор, сравнивающий объекты по полю district, а затем по полю name
                    Comparator<City> comparator1 = Comparator
                            .comparing(City::getDistrict)
                            .thenComparing(City::getName);
                    // Сортируем список
                    Collections.sort(cityList, comparator1);
                    //Вывод списка
                    for (City city : cityList) {
                        System.out.println(city.toString());
                    }
                    break;
                case ("3"):
                    // Преобразуем список в массив
                    City[] cityArray = cityList.toArray(new City[cityList.size()]);
                    int indexCityWithMaxPopulation =0;
                    int maxPopulation = 0;
                    for (int i = 0; i<cityArray.length;i++) {
                        if(maxPopulation<Integer.parseInt(cityArray[i].getPopulation())){
                            maxPopulation = Integer.parseInt(cityArray[i].getPopulation());
                            indexCityWithMaxPopulation = i;
                        }
                    }
                    //Вывод города с наибольшим числом жителей.
                    System.out.println(cityArray[indexCityWithMaxPopulation].toString());
                    break;
                case ("4"):
                    //Заполнение карты списков городов
                    Map<String, List<City>> regions = new HashMap<>();
                    for (City city: cityList){
                        if(regions.containsKey(city.region)){
                            regions.get(city.region).add(city);
                        } else{
                            regions.put(city.region, new ArrayList<City>());
                            regions.get(city.region).add(city);
                        }
                    }
                    //Вывод регионов и кол-во городов в них.
                    for (Map.Entry<String, List<City>> region : regions.entrySet()) {
                        String key = region.getKey();
                        Integer value = region.getValue().size();
                        System.out.println("Регион: " + key + ", Кол-во городов " + value);
                    }
                    break;
            }
        } catch (Exception e) {
            System.err.println(e);
        }
    }

    //Парсинг csv файла
    private static ArrayList<City> parseCSV(String filePath) throws IOException {
        ArrayList<City> cityList = new ArrayList<City>();
        //Загрузка строк из файла
        List<String> fileLines = Files.readAllLines(Paths.get(filePath));
        //Цикл по строкам
        for (String fileLine : fileLines) {
            String[] splitedText = fileLine.split(";");
            ArrayList<String> columnList = new ArrayList<String>();
            //Цикл по колонкам
            for (int i = 0; i < splitedText.length; i++) {
                //Если колонка начинается на кавычки или заканчиваеться на кавычки
                columnList.add(splitedText[i]);
            }
            if (columnList.size() == 6) {
                //Заполнение city
                cityList.add(new City(columnList.get(1), columnList.get(2), columnList.get(3), columnList.get(4), columnList.get(5)));
            } else if (columnList.get(0) != null) {
                System.out.println("Запись в строке " + columnList.get(0) + " содержит не полный набор данных");
            }
        }
        return cityList;
    }
}