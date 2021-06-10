package data;

public class TextConstants {
    public static final String INTRODUCTION_FAQ = "1. Вводная информация.\n" +
            "Программа позволяет производить учет ресурсов агрегатов и планера самолёта, добавлять и редактировать информацию об ИАК, " +
            "бортах и их составляющих частях (пока что только в части СПиСо - МКУ, МПУ и Л-029)." +
            "\nПрограмму нельзя переносить из папки, в которой находятся jdk-11 и файл программы с расширением .exe\n" +
            "Если при вводе данных в поле не дает ввести данные - проверьте раскладку клавиатуры.\n" +
            "Кроме этого в числовые поля запрещен ввод символов отличных от цифр.";

    public static final String PREPAIRING_TO_WORK_FAQ = "2. Начало работы.\n" +
            "Необходимо создать хотя бы одного инженера. Заполняем звание из выпадающего списка, записываем ФИО в удобном формате. " +
            "Хоть Иванов Иван Иванович, хоть Иванов И.И.\n" +
            "После создания инженера переходим на вкладку \"Список агрегатов\" и заполняем данные об агрегатах.\n" +
            "Например, из выпадающего списка выбираем \"9А-829К2\" и заполняем информацию о номере, наработке и ресурсах в часах, " +
            "а также о том, какие работы ближайшие. Добавить агрегат можно их вкладки \"Список ВС\". " +
            "Создаем новую запись о самолёте и выбираем агрегаты из списка ранее добавленных, которые на нем стоят или создаем новые." +
            "Заполняем информацию об ИАК, которую добавляли в самом начале.";

    public static final String WORK_INSTRUCTIONS_FAQ = "3. Контроль ресурса.\n" +
            "При каждом запуске программы или изменении данных о ресурсе (добавили налётанные часы, например) " +
            "программа автоматически пересчитает данные с учетом внесенных изменений " +
            "Эту информацию можно увидеть как во вкладке \"Список ВС\", кликнув на любую запись из списка, так и во вкладке \"Список агрегатов\"";

    public static final String SAVE_AND_LOAD_FAQ = "4. Хранение данных." +
            "При первом запуске никаких данных в программе нет, но все введенные данные программа записывает " +
            "и при последующем использовании \"помнит\" произведенные изменения.);\n" +
            "При первом запуске программы она создаст три файла с расширением \".ae\"." +
            "\nВНИМАНИЕ: В этих файлах хранится вся информация о самолетах, инженерах и агрегатах, если эти файлы удалить или переместить - информация будет потеряна.";

    public static final String CONTACTS_AND_FEEDBACK_FAQ = "5. Предложения и пожелания.\n" +
            "Если в ходе работы программы возникли вопросы или проблемы, то вы знаете как со мной связаться.\n" +
            "А если не знаете, то пишите в telegram : @peg2sus";

    public static final String NO_AIRCRAFT_RECORDS = "Нет ни одной записи.\n" +
            "Добавьте хотя бы один самолёт.";

    public static final String NO_ENGINEERS_RECORDS = "Не введены данные об инженерах.\n" +
            "Добавьте хотя бы одного человека.";

    public static final String NO_COMPONENTS_RECORDS = "Не введены данные об агрегатах.\n" +
            "Добавьте хотя бы один агрегат";

    public static final String SELECT_ENGINEERS_RANK = "Выбери звание инженера!";

    public static final String UNATTACHED_FROM_AIRCRAFT = "Снят с ВС. На хранении.";

    public static final String ADDITIONAL_OPERATING_ADDED = "Наработка успешно добавлена.";

    public static final String NO_ENGINEER_ATTACHED = "За данным ВС не закреплен ни один инженер СПиСО";

    public static final String TITLE = "Учет ресурсов для ВС типа изд.70";

    public static final String SIMULTANEOUSLY_INSTALLATION_MKU_MPU = "Запрещена одновременная установка изд. 9А-829К2 и 9А-829К3";

    public static final String MAX_COUNT_OF_COMPONENTS = "На ВС уже установлено максимально допустимое кол-во изд.";

    public static final String COMPONENT_ALREADY_EXISTS = "Агрегат с таким номером уже существует. Проверьте вводимые данные.";

    public static final String ATTENTION = "Внимание!";

    public static final String EMPTY_FIELD_WARNING = "Проверьте, что все поля заполнены.";

    public static final String AIRCRAFT_DUPLICATE = "Попытка дублирования записи.\nВС с таким номером или именем уже создан.";

    public static final String  REG_NUMBER_CHECK = "Регистрационный номер самолета должен соответствовать формату RF-*****!\nВведите только цифры номера, \"RF-\" будет подставлен автоматически.";

    public static final String NO_COMPLIANT_FOR_L029 = "Не применимо для Л-029";
}
