INSERT INTO assignment_topic (
    creation_date,
    topic_de, topic_ru, topic_en,
    description_de, description_ru, description_en,
    keywords_de, keywords_ru, keywords_en,
    active
)
VALUES
(
    UTC_TIMESTAMP(),
    'Dein Hobby',
    'Твое хобби',
    'Your Hobby',
    'Beschreiben Sie Ihr Hobby. Wann haben Sie damit angefangen? Warum mögen Sie es? Geben Sie Beispiele und beschreiben Sie, welche Materialien oder Werkzeuge Sie verwenden.',
    'Опишите своё хобби. Когда вы начали заниматься им? Почему оно вам нравится? Приведите примеры и опишите, какие материалы или инструменты вы используете.',
    'Describe your hobby. When did you start it? Why do you like it? Provide examples and describe what materials or tools you use.',
    'Ich liebe..., Mein Hobby erlaubt es mir..., Seit ich angefangen habe...
spielen, zeichnen, Musik, lesen, Sport, sammeln',
    'Ich liebe... (Я люблю...), Mein Hobby erlaubt es mir... (Мое хобби позволяет мне...), Seit ich angefangen habe... (С тех пор, как я начал...)
spielen (играть), zeichnen (рисовать), Musik (музыка), lesen (читать), Sport (спорт), sammeln (коллекционировать)',
    'Ich liebe... (I love...), Mein Hobby erlaubt es mir... (My hobby allows me to...), Seit ich angefangen habe... (Ever since I started...)
spielen (to play), zeichnen (to draw), Musik (music), lesen (to read), Sport (sports), sammeln (to collect)',
    true
),
(
    UTC_TIMESTAMP(),
    'Eine unvergessliche Reise',
    'Запоминающееся путешествие',
    'A Memorable Trip',
    'Erzählen Sie von einer unvergesslichen Reise. Wohin sind Sie gereist? Mit wem? Welche Orte haben Sie besucht?',
    'Расскажите о путешествии, которое оставило яркие впечатления. Куда вы ездили? С кем? Какие места посетили?',
    'Describe a trip that left a lasting impression. Where did you go? With whom? What places did you visit?',
    'Ich werde niemals vergessen..., Es war erstaunlich..., Die Reise hat mein Leben verändert...
reisen, Besichtigung, Natur, Abenteuer, Kultur',
    'Ich werde niemals vergessen... (Я никогда не забуду...), Es war erstaunlich... (Это было удивительно...), Die Reise hat mein Leben verändert... (Путешествие изменило мою жизнь...)
reisen (путешествовать), Besichtigung (экскурсия), Natur (природа), Abenteuer (приключение), Kultur (культура)',
    'Ich werde niemals vergessen... (I will never forget...), Es war erstaunlich... (It was amazing...), Die Reise hat mein Leben verändert... (The trip changed my life...)
reisen (to travel), Besichtigung (sightseeing), Natur (nature), Abenteuer (adventure), Kultur (culture)',
    true
),
(
    UTC_TIMESTAMP(),
    'Ein typischer Tag',
    'Обычный день',
    'A Typical Day',
    'Beschreiben Sie einen typischen Tag. Wann wachen Sie auf? Was machen Sie tagsüber? Welche Routinen haben Sie?',
    'Опишите свой обычный день. Во сколько вы просыпаетесь? Чем занимаетесь? Какие у вас ежедневные рутинные занятия?',
    'Describe a typical day in your life. What time do you wake up? What do you do during the day? What are your routines?',
    'Jeden Morgen..., Tagsüber mache ich..., Mein Tag beginnt mit...
aufwachen, frühstücken, arbeiten, entspannen, schlafen',
    'Jeden Morgen... (Каждое утро...), Tagsüber mache ich... (В течение дня я...), Mein Tag beginnt mit... (Мой день начинается с...)
aufwachen (просыпаться), frühstücken (завтракать), arbeiten (работать), entspannen (отдыхать), schlafen (спать)',
    'Jeden Morgen... (Every morning...), Tagsüber mache ich... (During the day I...), Mein Tag beginnt mit... (My day starts with...)
aufwachen (to wake up), frühstücken (to have breakfast), arbeiten (to work), entspannen (to relax), schlafen (to sleep)',
    true
),
(
    UTC_TIMESTAMP(),
    'Dein Lieblingsessen',
    'Твое любимое блюдо',
    'Your Favorite Meal',
    'Beschreiben Sie Ihr Lieblingsessen. Was ist es? Warum mögen Sie es? Erklären Sie, wie es zubereitet wird und wann Sie es bevorzugen.',
    'Опишите своё любимое блюдо. Что это за блюдо? Почему оно вам нравится? Объясните, как его готовят и когда вы его предпочитаете.',
    'Describe your favorite meal. What is it? Why do you like it? Explain how it is prepared and when you prefer to eat it.',
    'Ich liebe..., Dieses Gericht erfreut mich immer..., Die Zubereitung dieses Gerichts...
kochen, lecker, Tradition, Rezept, Nachspeise',
    'Ich liebe... (Я обожаю...), Dieses Gericht erfreut mich immer... (Это блюдо всегда радует меня...), Die Zubereitung dieses Gerichts... (Приготовление этого блюда...)
kochen (готовить), lecker (вкусный), Tradition (традиция), Rezept (рецепт), Nachspeise (десерт)',
    'Ich liebe... (I adore...), Dieses Gericht erfreut mich immer... (This dish always delights me...), Die Zubereitung dieses Gerichts... (The preparation of this meal...)
kochen (to cook), lecker (delicious), Tradition (tradition), Rezept (recipe), Nachspeise (dessert)',
    true
),
(
    UTC_TIMESTAMP(),
    'Dein Traumberuf',
    'Твоя работа мечты',
    'Your Dream Job',
    'Beschreiben Sie Ihren Traumberuf. Was möchten Sie tun? Warum ist dieser Beruf besonders für Sie? Welche Fähigkeiten sind erforderlich?',
    'Опишите работу своей мечты. Чем бы вы хотели заниматься? Почему эта профессия для вас особенная? Какие навыки для нее необходимы?',
    'Describe your dream job. What would you like to do? Why is this profession special to you? What skills are required?',
    'Ich träume davon zu arbeiten..., Mein idealer Arbeitstag..., Dieser Beruf inspiriert mich...
arbeiten, Karriere, Traum, Erfolg, Entwicklung',
    'Ich träume davon zu arbeiten... (Я мечтаю работать...), Mein idealer Arbeitstag... (Мой идеальный рабочий день...), Dieser Beruf inspiriert mich... (Эта профессия вдохновляет меня...)
arbeiten (работать), Karriere (карьера), Traum (мечта), Erfolg (успех), Entwicklung (развитие)',
    'Ich träume davon zu arbeiten... (I dream of working...), Mein idealer Arbeitstag... (My ideal workday...), Dieser Beruf inspiriert mich... (This job inspires me...)
arbeiten (to work), Karriere (career), Traum (dream), Erfolg (success), Entwicklung (development)',
    true
);
