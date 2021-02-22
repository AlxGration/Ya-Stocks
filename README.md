# Ya Stocks

Android application (testing task for Yandex MDS)

## Краткие факты

- Общая архитектура приложения MVVM

- В качестве API был выбран сервис [MBOUM](https://mboum.com/api/documentation)

- Для запросов используется Retrofit2

- Результаты запросов сохраняются в БД Realm. (можно зайти в приложение без интернета)

- Запрос на обновление котировок каждые 10 сек (через Timer)




Экран 1 (Главный):

- Во вкладке Stocks отображает спиосок наиболее торгуемых акций, во вкладке Favourite - избранные.

- AppBarLayout (SearchView & кастомный TablLayout) и ViewPager для 2-ух фрагментов (Stocks & Favourite).

- Во фрагменте Stocks используется RecyclerView c кастомным адаптером и подпиской на LiveData.

- фрагмент Favourite аналогичен.

Экран 2 (Информация об акции):

- по разметке аналогичен 1ому экрану

- тут можно добавить акцию в избранное



## API

BaseUrl = https://mboum.com/api/v1/

Токен передается в хэдере запроса с ключом X-Mboum-Secret

- Получение самых торгуемых котировок: 
    
      MostActive (co/collections/?list=most_actives)


