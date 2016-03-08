package ru.kvisaz.bashreader.model;

/**
 *  Menu mapping for view
 */
public class BashMenu {

    public static final String bundleTopicTag = "topic";
    public static final String bundlePageCodeTag = "pagecode";

    public static final BashTopic[] items = {
            new BashTopic(0,BashPageType.Page,"Свежие"),
            new BashTopic(1,BashPageType.Random,"Случайные"),
            new BashTopic(2,BashPageType.ByRating,"По рейтингу"),
            new BashTopic(3,BashPageType.Abyss,"Бездна"),
            new BashTopic(4,BashPageType.AbyssTop,"Топ Бездны"),
            new BashTopic(5,BashPageType.AbyssBest,"Лучшее Бездны"),
            new BashTopic(6,BashPageType.Comics,"Комиксы"),
    };

    public static String[] getNamesArray(){
        String[] arr = new String[items.length];
        int i = 0;
        for(BashTopic item: items){
            arr[i] = item.title;
            i++;
        }
        return arr;
    }

    public static BashPageType getType(int topicNumber) {
        return items[topicNumber].type;
    }


    public static String getTitle(int topicNumber) {
        if(topicNumber < 0 || topicNumber >= items.length) return "";
        return items[topicNumber].title;
    }

    public static class BashTopic{
        public int menuItemNumber;
        public BashPageType type;
        public String title;

        BashTopic(int aNumber, BashPageType aType, String aTitle){
            menuItemNumber = aNumber;
            type = aType;
            title = aTitle;
        }
    }

}
