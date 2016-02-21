package ru.kvisaz.bashreader.custom;

import java.util.ArrayList;


public class BashPageTest extends BashPage {

    public BashPageTest(){

        quotes = new ArrayList<>();

        // delete this comment

        int quote_id = 1;
        String date = "2016-01-10 09:12"; // ! Date отдаётся в Locale
        int rating = 3915;
        String text = "Мелкий (8 лет) после экскурсии в Пушкин: ] \n - Ма, да Пушкин вообще сельскохозяйственный техникум закончил! \n Расследование показало: Царскосельский - ключевое слово \"сельский\", лицей=техникум... \n С учётом современных реалий и не поспоришь!";

        quotes.add(new BashQuote(quote_id,text,date,rating));

        quote_id = 2;
        date = "2016-01-10 08:46";
        rating = 416;
        text = "Книжный магазин. Ищу Буджолд, слышу краем голоса разговор по телефону:\n" +
                "- А что вам точно задали прочитать о Шерлоке? Да? А точнее? Мориарти? Сейчас спрошу.\n" +
                "Подзывается консультант, задается тихо вопрос. Консультант так же тихо, будто смущенно отвечает. И снова тетечка по телефону:\n" +
                "- Знаешь, а Мориарти - это герой, а не автор. Так что уточни у учительницы, какого Мориарти нужно читать.\n" +
                "\n" +
                "Вот никогда не думала, что доживу до такого, а поди ж ты. Или это сериал так влияет?";

        quotes.add(new BashQuote(quote_id,text,date,rating));

        quote_id = 3;
        date = "2016-01-10 08:45";
        rating = 337;
        text = "Из ЖЖ scinquisitor\n" +
                "\n" +
                "the_toad:\n" +
                "\n" +
                "От что меня восхищает, так это люди, знающие всё про законы вселенной.\n" +
                "Вот поручи такому простенький пятистадийный синтез - он и колбу утеряет, и руки порежет.\n" +
                "Вселенная - другое дело, тут он мастак.";

        quotes.add(new BashQuote(quote_id,text,date,rating));

        quote_id = 4;
        date = "2016-01-10 08:13";
        rating = 368;
        text = "xxx: Буфетчица поезда Львов-Харьков обсуждает по айфону как она была на Таиланде. Вопрос. Кто из нас двоих программист";

        quotes.add(new BashQuote(quote_id,text,date,rating));

        quote_id = 5;
        date = "2016-01-10 08:12";
        rating = 711;
        text = "misha_makferson: Странная реклама. \"Кольцо Всевластия\n" +
                "Карбид вольфрама. Всего 490 руб. Быстрая доставка, самовывоз. Подробнее по ссылке\n" +
                "\n" +
                "den_stranger: Упали цены! Раньше полторы тыщи стоило!\n" +
                "\n" +
                "khaa_aleс: раньше доставка сложнее была - четыре дядьки пёхом тащили";

        quotes.add(new BashQuote(quote_id,text,date,rating));

        quote_id = 6;
        date = "2016-01-09 08:45";
        rating = 2560;
        text = "aaa: Есть же специальные для тачскринов, там кончики пальцев сделаны из проводящего материала.\n" +
                "bbb: Оо, как они называются? А то я видел только вязаные igloves, которые рвутся через месяц использования, И в мороз в них не походишь.\n" +
                "ccc: Купите металлизированные нитки и сделайте несколько стежков под подушечками пальцев. Начинайте прошивать изнутри оставив конец нитки после узелка подлиннее, чтобы обеспечить лучшую проводимость.\n" +
                "ddd: — Как ты работаешь с айпадом в перчатках?\n" +
                "— Я их перепрошил.";

        quotes.add(new BashQuote(quote_id,text,date,rating));

        quote_id = 7;
        date = "2016-01-09 08:44";
        rating = 966;
        text = "Из отзывов на бетономешалку:\n" +
                "\n" +
                "За время и силы, затраченное на процесс сборки эта бетономешалка как минимум сама должна ездить на карьер, добывать составляющие для бетона, строить опалубку и вязать арматуру.";

        quotes.add(new BashQuote(quote_id,text,date,rating));

        quote_id = 8;
        date = "2016-01-09 08:14";
        rating = 3421;
        text = "Тут текст потерялся, но я его заполнил. Все равно это плейсхолдер, чтобы место занять.";

        quotes.add(new BashQuote(quote_id,text,date,rating));

        quote_id = 9;
        date = "2016-01-09 08:13";
        rating = 1267;
        text = "Чтобы отвлечься от эротических мыслей и уснуть, решил считать овец, скачущих через изгородь. Овцы эротично взбрыкивали попами, а за ними скакали бараны с огромными эрегированными органами. Что я, не хозяин своим мозгам? Минут через пять борьбы с этим кино поднялся и пошёл жрать...";

        quotes.add(new BashQuote(quote_id,text,date,rating));

        quote_id = 10;
        date = "2016-01-09 08:12";
        rating = 568;
        text = "xxx: на свете рождаются люди со встроенной системой обфускации кода\n" +
                "xxx: обычно мы их называем мудаками";

        quotes.add(new BashQuote(quote_id,text,date,rating));

        quote_id = 11;
        date = "2016-01-09 08:11";
        rating = 3688;
        text = "из обсуждения рюкзака для оружия:\n" +
                "xxx: А экономным посоветую сходить в музыкальный магазин, можно выбрать беспалевный чехол (или вообще твердый кейс) почти под любую длину ствола. У нас на остановке, где стрельбище, пол автобуса «музыкантов» выходит. =)";

        quotes.add(new BashQuote(quote_id,text,date,rating));

        quote_id = 12;
        date = "2016-01-08 09:12";
        rating = 2371;
        text = "xxx:\n" +
                "Экспериментирую с генератором имён-фамилий. Выбираю англо-ирландские имена, жму \"генерировать\" - он мне выдаёт: Томас O'Мур. А моего кота вот уже третий год зовут Томасом, и, кажется, только что он обрёл ещё и фамилию :D";

        quotes.add(new BashQuote(quote_id,text,date,rating));

        quote_id = 13;
        date = "2016-01-08 08:46";
        rating = 2827;
        text = "Итак, на этой неделе я провела свой первый экзамен. Все прошло более или менее нормально, хоть и пришлось поставить пару двоек. Был правда момент, когда на мой вопрос от какого языка произошел С++ мне сказали \"1с\" и я не знала, чего мне хочется больше - биться головой о стол или бить головой о стол студента..особенно, когда его следующий вариант был \"с+\".";

        quotes.add(new BashQuote(quote_id,text,date,rating));

        quote_id = 14;
        date = "2016-01-08 08:12";
        rating = 3378;
        text = "Пока сидел под дверью аудитории в универе - подслушал беседу наших магистров.\n" +
                "- Мы когда выписывались из общаги - нам вахтерша говорит: ой, как жаль, что вы съезжаете, вы такие тихие, не видно, не слышно вас весь год, а до вас тут такие проститутки жили, каждую ночь шумели! Ну мы ей не стали говорить, что мы тут четыре года живем...";

        quotes.add(new BashQuote(quote_id,text,date,rating));

        quote_id = 15;
        date = "2016-01-08 08:11";
        rating = 1726;
        text = "с форума, тема обсуждения личных авто:\n" +
                "ХХХ: Ты можешь оставить свою вольву с ключами в замке зажигания и открытыми дверями, а она все равно не заинтересует угонщиков. Это я как человек у которого было 3 вольвы говорю.)\n" +
                "YYY: Я тебе сто раз говорил: избавиться от машины с чьим-то трупом на заднем сиденье не так уж легко. Тем более, если перекладывать его в каждую последующую тачку.";

        quotes.add(new BashQuote(quote_id,text,date,rating));

        quote_id = 16;
        date = "2016-01-08 08:11";
        rating = 1180;
        text = "xxx: Позвонил товарищ, и рассказал, какие у него на работе мудаки. Через два часа перезвонил, и чтобы усилить мое впечатление, рассказал еще раз.";

        quotes.add(new BashQuote(quote_id,text,date,rating));

        quote_id = 17;
        date = "2016-01-07 08:44";
        rating = 2796;
        text = "xxx: да у неё с реал лайф вообще большие проблемы.\n" +
                "ххх: знаешь, как она понимает, что пора спать?\n" +
                "ууу: как же?\n" +
                "ххх: она когда в симс играет, то если замечает, что, укладывая сима спать, люто ему завидует, значит, кажется, уже ночь.";

        quotes.add(new BashQuote(quote_id,text,date,rating));

        quote_id = 18;
        date = "2016-01-07 08:44";
        rating = 1376;
        text = "ххх: Можно ли как-то привлечь коллекторов, если попортили дверь?\n" +
                "ууу: можно, это порча имущества\n" +
                "ххх: а если насрали под дверью?!\n" +
                "ууу: не нельзя, это уже не порча, это инсталляция :)";

        quotes.add(new BashQuote(quote_id,text,date,rating));

        quote_id = 19;
        date = "2016-01-07 08:12";
        rating = 2925;
        text = "Только что к нам в офис пришла девушка с целью обновления базы нормативно-технической документации, а заодно подарила всем календарики и музыкальные диски с логотипами ее конторы. Так вот включив музыку, я услышал спокойную умиротворенную мелодию, исполняемую на классических инструментах - и была она до боли знакома, но я все никак не мог вспомнить где же ее слышал. Но потом меня осенило - это была мелодия песни \"Ленинград\" - Любит наш народ всякое говно. Троли 80лвл.";

        quotes.add(new BashQuote(quote_id,text,date,rating));

        quote_id = 20;
        date = "2016-01-07 08:11";
        rating = 2306;
        text = "она: Я люблю тебя. И спать! Но спать всё-таки больше...\n" +
                "он: Это потому что с ним можно 10 часов подряд, а со мной нет?";

        quotes.add(new BashQuote(quote_id,text,date,rating));

        quote_id = 21;
        date = "2016-01-07 08:11";
        rating = 2136;
        text = "С хабра, из обсуждения статьи, что Марк Цукерберг хочет домашний искуственный интеллект как Джарвис из \"Железного человека\":\n" +
                "\n" +
                "xxx: Почему бы и нет? По-моему гораздо удобнее сказать «Джарвис, включи последний альбом %группы%» или «Джарвис, уменьши яркость настенных ламп на 30%», Находясь в любом месте квартиры / дома.\n" +
                "\n" +
                "yyy: А Джарвис такой «А ты купил последний альбом группы %группы% или тебе из ВКонтактика запилить?»";

        quotes.add(new BashQuote(quote_id,text,date,rating));

        quote_id = 22;
        date = "2016-01-06 09:45";
        rating = 1357;
        text = "На работе надо было в цех срочно перебросить программу на ЧПУ станок. Поскольку оборонка- все ЮСБ отрубили. Сеть по заводу постоянно вылетает, как и в этот раз. Нашёл кучу старых дискет, флоппи привод. Всё установил, скинул прогу. Поскольку как-то лишился флэшки своим безрассудством, долго искал где БЕЗОПАСНОЕ ИЗВЛЕЧЕНИЕ ДИСКЕТЫ!";

        quotes.add(new BashQuote(quote_id,text,date,rating));

        quote_id = 23;
        date = "2016-01-06 09:45";
        rating = 3185;
        text = "xxx: Миша в поисках любви - бронепоезд с ромашкой на фаре. Года два назад подцепил девушку в клубе, привез домой, а она ему: \"Ну какой секс, не надо портить романтичный момент. Мы только познакомились, это неприлично. Давай поговорим.\" Чаю попили и она упорхнула на такси. Только номер забыла оставить.\n" +
                "ххх: Он ее искал месяц. Рассказывал, какая она приличная и особенная. Объявления вешал на домах, весь контакт перерыл. Каждую пятницу как штык в том клубе.\n" +
                "xxx: Потом смс пришло \"Месячные у меня начались тогда, маньяк\". Она ли это или из наших кто сжалился история умалчивает, перезванивать наш романтик не стал.";

        quotes.add(new BashQuote(quote_id,text,date,rating));

        quote_id = 24;
        date = "2016-01-06 09:13";
        rating = 2284;
        text = "X: Где скачать омерзительную восьмёрку?\n" +
                "Y: Посмотри в папке с образами, там и восьмерка, и семёрка...";

        quotes.add(new BashQuote(quote_id,text,date,rating));

        quote_id = 25;
        date = "2016-01-06 08:46";
        rating = 1957;
        text = "Я: Распакуй архив, только не на системный диск\n" +
                "Она: Хорошо, тогда я его на рабочий стол распакую";

        quotes.add(new BashQuote(quote_id,text,date,rating));

        quote_id = 26;
        date = "2016-01-06 08:13";
        rating = 2348;
        text = "Затупил - это когда выходишь в три часа ночи погулять с собакой и глядя на закрывающуюся дверь подъезда вспоминаешь, что ключ от домофона размагнитился и его надо было поменять. Аккумулятор ты этим же вечером снял, чтобы подзарядить и даже в машине теперь не погреешься. Телефон тоже дома, а кроме тебя в квартире никого. и -20 по Цельцию...";

        quotes.add(new BashQuote(quote_id,text,date,rating));



    }

}
