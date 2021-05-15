package data;

public class TextConstants {
    public static final String INTRODUCTION_FAQ = "1. Вводная информация.\n" +
            "Программа позволяет производить учет ресурсов агрегатов и планера самолёта, добавлять и редактировать информацию об ИАК, " +
            "бортах и их составляющих частях (пока что только в части СД - двигатель, планер, ВСУ).";

    public static final String PREPAIRING_TO_WORK_FAQ = "2. Начало работы.\n" +
            "Необходимо создать хотя бы одного инженера. Заполняем звание из выпадающего списка, записываем ФИО в удобном формате. " +
            "Хоть Иванов Иван Иванович, хоть Иванов И.И.\n" +
            "После создания инженера переходим на вкладку \"Список агрегатов\" и заполняем данные об агрегатах.\n" +
            "Например, из выпадающего списка выбираем \"двигатель\" и заполняем информацию о номере, наработке и ресурсах в часах, " +
            "а также о том, какие работы ближайшие. После заполнения агрегатов нажимаем на вкладку \"Список ВС\". " +
            "Создаем новую запись о самолёте и выбираем агрегаты из списка ранее добавленных, которые на нем стоят. " +
            "Можно не заполнять все поля, например, если на борту нет движка или еще что-то отсутствует.\n" +
            "Заполняем информацию об ИАК, которую добавляли в самом начале.";

    public static final String WORK_INSTRUCTIONS_FAQ = "3. Контроль ресурса.\n" +
            "При каждом запуске программы или изменении данных о ресурсе (добавили налётанные часы, продлили ресурс и т.д.) " +
            "программа автоматически пересчитает сколько времени осталось до проведения работ на каждом из агрегатов. " +
            "Эту информацию можно увидеть как во вкладке \"Список ВС\", кликнув на любую запись из списка, так и во вкладке \"Список агрегатов\"";

    public static final String SAVE_AND_LOAD_FAQ = "4. Хранение данных." +
            "При первом запуске никаких данных в программе нет, но все введенные данные программа записывает " +
            "и при последующем использовании \"помнит\" произведенные изменения.);\n";

    public static final String CONTACTS_AND_FEEDBACK_FAQ = "5. Предложения и пожелания.\n" +
            "Если в ходе работы программы возникли вопросы или проблемы, то вы знаете как со мной связаться.\n" +
            "А если не знаете, то пишите в telegram : @peg2sus";


}
