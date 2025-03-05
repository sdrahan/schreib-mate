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
    'Mein Hobby',
    'Моё хобби',
    'My Hobby',
    'Schreiben Sie eine E-Mail an einen Freund oder eine Freundin und erzählen Sie von Ihrem Hobby. Gehen Sie dabei auf folgende Punkte ein:\n\n- Wann haben Sie damit angefangen?\n- Warum mögen Sie es?\n- Welche Materialien oder Werkzeuge verwenden Sie?\n- Welche Vorteile bringt Ihnen Ihr Hobby?',
    'Напишите письмо другу или подруге и расскажите о своем хобби. Затроньте следующие моменты:\n\n- Когда вы начали заниматься им?\n- Почему оно вам нравится?\n- Какие материалы или инструменты вы используете?\n- Какие преимущества вам дает это хобби?',
    'Write an email to a friend and talk about your hobby. Address the following points:\n\n- When did you start it?\n- Why do you like it?\n- What materials or tools do you use?\n- What benefits does your hobby bring you?',
    '• Mein Hobby ist …\n• Ich habe damit angefangen, weil …\n• Ich benutze …\n• Besonders mag ich daran …\n• Was ist dein Hobby?',
    '• Mein Hobby ist … (Моё хобби - ...)\n• Ich habe damit angefangen, weil … (Я начал им заниматься, потому что ...)\n• Ich benutze … (Я использую ...)\n• Besonders mag ich daran … (Мне особенно нравится в этом ...)\n• Was ist dein Hobby? (Какое у тебя хобби?)',
    '• Mein Hobby ist … (My hobby is ...)\n• Ich habe damit angefangen, weil … (I started it because ...)\n• Ich benutze … (I use ...)\n• Besonders mag ich daran … (What I especially like about it is ...)\n• Was ist dein Hobby? (What is your hobby?)',
    TRUE
),
(
    UTC_TIMESTAMP(),
    'Eine unvergessliche Reise',
    'Запоминающееся путешествие',
    'A Memorable Trip',
    'Schreiben Sie eine E-Mail an einen Freund oder eine Freundin und berichten Sie über eine unvergessliche Reise. Gehen Sie dabei auf folgende Punkte ein:\n\n- Wohin sind Sie gereist?\n- Mit wem?\n- Welche Orte haben Sie besucht?\n- Warum war diese Reise besonders?',
    'Напишите письмо другу или подруге и расскажите о незабываемом путешествии. Затроньте следующие моменты:\n\n- Куда вы ездили?\n- С кем?\n- Какие места вы посетили?\n- Почему это путешествие было особенным?',
    'Write an email to a friend and talk about a memorable trip. Address the following points:\n\n- Where did you travel?\n- With whom?\n- What places did you visit?\n- Why was this trip special?',
    '• Ich werde niemals vergessen …\n• Besonders schön war …\n• Wir haben … besucht\n• Diese Reise war besonders, weil …\n• Wohin möchtest du gerne reisen?',
    '• Ich werde niemals vergessen … (Я никогда не забуду ...)\n• Besonders schön war … (Особенно красиво было ...)\n• Wir haben … besucht (Мы посетили ...)\n• Diese Reise war besonders, weil … (Это путешествие было особенным, потому что ...)\n• Wohin möchtest du gerne reisen? (Куда ты хотел бы поехать?)',
    '• Ich werde niemals vergessen … (I will never forget ...)\n• Besonders schön war … (It was especially beautiful ...)\n• Wir haben … besucht (We visited ...)\n• Diese Reise war besonders, weil … (This trip was special because ...)\n• Wohin möchtest du gerne reisen? (Where would you like to travel?)',
    TRUE
),
(
    UTC_TIMESTAMP(),
    'Ein typischer Tag',
    'Обычный день',
    'A Typical Day',
    'Schreiben Sie eine E-Mail an einen Freund oder eine Freundin und berichten Sie über Ihren typischen Tag. Gehen Sie dabei auf folgende Punkte ein:\n\n- Wann wachen Sie auf?\n- Was machen Sie tagsüber?\n- Welche Routinen haben Sie?\n- Wie endet Ihr Tag?',
    'Напишите письмо другу или подруге и расскажите о своем обычном дне. Затроньте следующие моменты:\n\n- Во сколько вы просыпаетесь?\n- Чем занимаетесь в течение дня?\n- Какие у вас ежедневные привычки?\n- Как заканчивается ваш день?',
    'Write an email to a friend and describe your typical day. Address the following points:\n\n- What time do you wake up?\n- What do you do during the day?\n- What are your daily routines?\n- How does your day end?',
    '• Jeden Morgen wache ich um … auf\n• Tagsüber mache ich …\n• Meine tägliche Routine ist …\n• Abends …\n• Wie sieht dein Tag aus?',
    '• Jeden Morgen wache ich um … auf (Каждое утро я просыпаюсь в ...)\n• Tagsüber mache ich … (В течение дня я занимаюсь ...)\n• Meine tägliche Routine ist … (Моя ежедневная рутина - ...)\n• Abends … (Вечером я ...)\n• Wie sieht dein Tag aus? (Как проходит твой день?)',
    '• Jeden Morgen wache ich um … auf (Every morning I wake up at ...)\n• Tagsüber mache ich … (During the day I do ...)\n• Meine tägliche Routine ist … (My daily routine is ...)\n• Abends … (In the evening I ...)\n• Wie sieht dein Tag aus? (What does your day look like?)',
    TRUE
),
(
    UTC_TIMESTAMP(),
    'Mein Lieblingsessen',
    'Моё любимое блюдо',
    'My Favorite Meal',
    'Schreiben Sie eine E-Mail an einen Freund oder eine Freundin und berichten Sie über Ihr Lieblingsessen. Gehen Sie dabei auf folgende Punkte ein:\n\n- Wie heißt das Gericht?\n- Warum mögen Sie es?\n- Wie wird es zubereitet?\n- Wann essen Sie es am liebsten?',
    'Напишите письмо другу или подруге и расскажите о своем любимом блюде. Затроньте следующие моменты:\n\n- Как называется это блюдо?\n- Почему оно вам нравится?\n- Как его готовят?\n- Когда вы предпочитаете его есть?',
    'Write an email to a friend and talk about your favorite meal. Address the following points:\n\n- What is the dish called?\n- Why do you like it?\n- How is it prepared?\n- When do you prefer to eat it?',
    '• Mein Lieblingsessen ist …\n• Besonders lecker finde ich …\n• Es wird aus … zubereitet\n• Ich esse es am liebsten …\n• Welches Essen magst du am meisten?',
    '• Mein Lieblingsessen ist … (Моё любимое блюдо - ...)\n• Besonders lecker finde ich … (Я считаю особенно вкусным ...)\n• Es wird aus … zubereitet (Оно готовится из ...)\n• Ich esse es am liebsten … (Я люблю есть его ...)\n• Welches Essen magst du am meisten? (Какое блюдо ты любишь больше всего?)',
    '• Mein Lieblingsessen ist … (My favorite meal is ...)\n• Besonders lecker finde ich … (I find it especially delicious ...)\n• Es wird aus … zubereitet (It is made from ...)\n• Ich esse es am liebsten … (I prefer to eat it ...)\n• Welches Essen magst du am meisten? (What food do you like the most?)',
    TRUE
)
,
(
    UTC_TIMESTAMP(),
    'Gesunde Ernährung',
    'Здоровое питание',
    'Healthy Nutrition',
    'Sie haben in einer Online-Zeitung einen Artikel über gesunde Ernährung gelesen. Schreiben Sie eine E-Mail an einen Freund oder eine Freundin und berichten Sie darüber. Gehen Sie dabei auf folgende Punkte ein: \n\n- Wo haben Sie den Artikel gelesen?\n- Welche Informationen fanden Sie interessant?\n- Welche Rolle spielt gesunde Ernährung in Ihrem Leben?\n- Welche Tipps haben Sie für eine gesunde Ernährung?',
    'Вы прочитали в онлайн-газете статью о здоровом питании. Напишите письмо другу или подруге и расскажите об этом. Затроньте следующие моменты: \n\n- Где вы прочитали эту статью?\n- Какие сведения вам показались интересными?\n- Какую роль играет здоровое питание в вашей жизни?\n- Какие советы у вас есть для здорового питания?',
    'You have read an article about healthy nutrition in an online newspaper. Write an email to a friend and tell them about it. Address the following points: \n\n- Where did you read the article?\n- What information did you find interesting?\n- What role does healthy nutrition play in your life?\n- What tips do you have for healthy eating?',
    '• Ich habe einen Artikel über … gelesen\n• Besonders interessant war …\n• In meinem Leben spielt … eine große Rolle\n• Mein Tipp für eine gesunde Ernährung ist …\n• Was denkst du darüber?',
    '• Ich habe einen Artikel über … gelesen (Я прочитал статью о ...)\n• Besonders interessant war … (Особенно интересно было ...)\n• In meinem Leben spielt … eine große Rolle (В моей жизни ... играет важную роль)\n• Mein Tipp für eine gesunde Ernährung ist … (Мой совет для здорового питания - ...)\n• Was denkst du darüber? (Что ты думаешь об этом?)',
    '• Ich habe einen Artikel über … gelesen (I have read an article about ...)\n• Besonders interessant war … (Especially interesting was ...)\n• In meinem Leben spielt … eine große Rolle (In my life, ... plays an important role)\n• Mein Tipp für eine gesunde Ernährung ist … (My tip for healthy eating is ...)\n• Was denkst du darüber? (What do you think about it?)',
    TRUE
),
(
    UTC_TIMESTAMP(),
    'Reisen und Urlaub',
    'Путешествия и отдых',
    'Travel and Vacation',
    'Sie haben kürzlich eine interessante Reise unternommen. Schreiben Sie eine E-Mail an einen Freund oder eine Freundin und erzählen Sie davon. Gehen Sie dabei auf folgende Punkte ein:\n\n- Wohin sind Sie gereist?\n- Was haben Sie dort erlebt?\n- Was hat Ihnen besonders gefallen?\n- Würden Sie diese Reise weiterempfehlen?',
    'Вы недавно совершили интересное путешествие. Напишите письмо другу или подруге и расскажите об этом. Затроньте следующие моменты:\n\n- Куда вы путешествовали?\n- Что вы там пережили?\n- Что вам особенно понравилось?\n- Порекомендовали бы вы это путешествие?',
    'You recently took an interesting trip. Write an email to a friend and tell them about it. Address the following points:\n\n- Where did you travel?\n- What did you experience there?\n- What did you like the most?\n- Would you recommend this trip?',
    '• Ich bin nach … gereist\n• Besonders schön war …\n• Ich habe viele neue Dinge erlebt\n• Ich empfehle diese Reise, weil …\n• Wohin reist du gerne?',
    '• Ich bin nach … gereist (Я путешествовал в ...)\n• Besonders schön war … (Особенно красиво было ...)\n• Ich habe viele neue Dinge erlebt (Я испытал много нового)\n• Ich empfehle diese Reise, weil … (Я рекомендую эту поездку, потому что ...)\n• Wohin reist du gerne? (Куда ты любишь путешествовать?)',
    '• Ich bin nach … gereist (I traveled to ...)\n• Besonders schön war … (Especially beautiful was ...)\n• Ich habe viele neue Dinge erlebt (I experienced many new things)\n• Ich empfehle diese Reise, weil … (I recommend this trip because ...)\n• Wohin reist du gerne? (Where do you like to travel?)',
    TRUE
),
(
    UTC_TIMESTAMP(),
    'Sport und Bewegung',
    'Спорт и физическая активность',
    'Sports and Exercise',
    'Sie haben angefangen, eine neue Sportart auszuüben. Schreiben Sie eine E-Mail an einen Freund oder eine Freundin und erzählen Sie davon. Gehen Sie dabei auf folgende Punkte ein:\n\n- Welche Sportart machen Sie?\n- Warum haben Sie damit angefangen?\n- Welche Vorteile sehen Sie darin?\n- Würden Sie es weiterempfehlen?',
    'Вы начали заниматься новым видом спорта. Напишите письмо другу или подруге и расскажите об этом. Затроньте следующие моменты:\n\n- Каким видом спорта вы занимаетесь?\n- Почему вы начали этим заниматься?\n- Какие преимущества вы видите?\n- Порекомендовали бы вы его другим?',
    'You have started a new sport. Write an email to a friend and tell them about it. Address the following points:\n\n- What sport are you doing?\n- Why did you start it?\n- What advantages do you see?\n- Would you recommend it?',
    '• Ich habe angefangen, … zu machen\n• Diese Sportart gefällt mir, weil …\n• Besonders gut finde ich, dass …\n• Sport ist wichtig, weil …\n• Machst du auch Sport?',
    '• Ich habe angefangen, … zu machen (Я начал заниматься ...)\n• Diese Sportart gefällt mir, weil … (Этот вид спорта мне нравится, потому что ...)\n• Besonders gut finde ich, dass … (Особенно мне нравится, что ...)\n• Sport ist wichtig, weil … (Спорт важен, потому что ...)\n• Machst du auch Sport? (Ты тоже занимаешься спортом?)',
    '• Ich habe angefangen, … zu machen (I started doing ...)\n• Diese Sportart gefällt mir, weil … (I like this sport because ...)\n• Besonders gut finde ich, dass … (I especially like that ...)\n• Sport ist wichtig, weil … (Sports are important because ...)\n• Machst du auch Sport? (Do you also do sports?)',
    TRUE
),
(
    UTC_TIMESTAMP(),
    'Feiertage und Traditionen',
    'Праздники и традиции',
    'Holidays and Traditions',
    'Sie haben kürzlich einen besonderen Feiertag gefeiert. Schreiben Sie eine E-Mail an einen Freund oder eine Freundin und berichten Sie darüber. Gehen Sie dabei auf folgende Punkte ein:\n\n- Welchen Feiertag haben Sie gefeiert?\n- Wie haben Sie ihn gefeiert?\n- Mit wem haben Sie gefeiert?\n- Welche Traditionen sind mit diesem Feiertag verbunden?',
    'Вы недавно праздновали особый праздник. Напишите письмо другу или подруге и расскажите об этом. Затроньте следующие моменты:\n\n- Какой праздник вы отмечали?\n- Как вы его праздновали?\n- С кем вы его отмечали?\n- Какие традиции связаны с этим праздником?',
    'You recently celebrated a special holiday. Write an email to a friend and tell them about it. Address the following points:\n\n- What holiday did you celebrate?\n- How did you celebrate it?\n- With whom did you celebrate it?\n- What traditions are associated with this holiday?',
    '• Ich habe … gefeiert\n• Es war ein schöner Tag, weil …\n• Besonders gut hat mir gefallen …\n• In meiner Familie ist es Tradition, …\n• Wie feierst du diesen Feiertag?',
    '• Ich habe … gefeiert (Я праздновал ...)\n• Es war ein schöner Tag, weil … (Это был хороший день, потому что ...)\n• Besonders gut hat mir gefallen … (Особенно мне понравилось ...)\n• In meiner Familie ist es Tradition, … (В моей семье традиция ...)\n• Wie feierst du diesen Feiertag? (Как ты празднуешь этот праздник?)',
    '• Ich habe … gefeiert (I celebrated ...)\n• Es war ein schöner Tag, weil … (It was a nice day because ...)\n• Besonders gut hat mir gefallen … (I especially liked ...)\n• In meiner Familie ist es Tradition, … (In my family, it is a tradition to ...)\n• Wie feierst du diesen Feiertag? (How do you celebrate this holiday?)',
    TRUE
),
(
    UTC_TIMESTAMP(),
    'Umwelt und Nachhaltigkeit',
    'Окружающая среда и устойчивое развитие',
    'Environment and Sustainability',
    'Sie haben einen Artikel über Umweltschutz gelesen. Schreiben Sie eine E-Mail an einen Freund oder eine Freundin und berichten Sie darüber. Gehen Sie dabei auf folgende Punkte ein:\n\n- Wo haben Sie den Artikel gelesen?\n- Welche Informationen fanden Sie besonders interessant?\n- Was tun Sie persönlich für die Umwelt?\n- Welche Tipps haben Sie für umweltfreundliches Verhalten?',
    'Вы прочитали статью об охране окружающей среды. Напишите письмо другу или подруге и расскажите об этом. Затроньте следующие моменты:\n\n- Где вы прочитали эту статью?\n- Какие сведения вам показались интересными?\n- Что вы лично делаете для экологии?\n- Какие советы у вас есть для экологически чистого образа жизни?',
    'You have read an article about environmental protection. Write an email to a friend and tell them about it. Address the following points:\n\n- Where did you read the article?\n- What information did you find interesting?\n- What do you personally do for the environment?\n- What tips do you have for eco-friendly behavior?',
    '• Ich habe einen Artikel über … gelesen\n• Besonders spannend fand ich …\n• Ich versuche, umweltfreundlich zu leben\n• Mein Tipp für den Umweltschutz ist …\n• Was denkst du darüber?',
    '• Ich habe einen Artikel über … gelesen (Я прочитал статью о ...)\n• Besonders spannend fand ich … (Особенно увлекательно было ...)\n• Ich versuche, umweltfreundlich zu leben (Я стараюсь жить экологично)\n• Mein Tipp für den Umweltschutz ist … (Мой совет по защите окружающей среды - ...)\n• Was denkst du darüber? (Что ты думаешь об этом?)',
    '• Ich habe einen Artikel über … gelesen (I read an article about ...)\n• Besonders spannend fand ich … (I found it especially fascinating ...)\n• Ich versuche, umweltfreundlich zu leben (I try to live eco-friendly)\n• Mein Tipp für den Umweltschutz ist … (My tip for environmental protection is ...)\n• Was denkst du darüber? (What do you think about it?)',
    TRUE
),
(
    UTC_TIMESTAMP(),
    'Beruf und Karriere',
    'Профессия и карьера',
    'Career and Job',
    'Sie haben vor Kurzem eine neue Arbeitsstelle oder ein Praktikum begonnen. Schreiben Sie eine E-Mail an einen Freund oder eine Freundin und berichten Sie darüber. Gehen Sie dabei auf folgende Punkte ein:\n\n- Wo arbeiten Sie?\n- Was sind Ihre Aufgaben?\n- Wie gefällt Ihnen die neue Tätigkeit?\n- Welche Pläne haben Sie für die Zukunft?',
    'Вы недавно начали новую работу или стажировку. Напишите письмо другу или подруге и расскажите об этом. Затроньте следующие моменты:\n\n- Где вы работаете?\n- Какие у вас обязанности?\n- Как вам нравится новая работа?\n- Какие у вас планы на будущее?',
    'You recently started a new job or internship. Write an email to a friend and tell them about it. Address the following points:\n\n- Where do you work?\n- What are your tasks?\n- How do you like your new job?\n- What are your future plans?',
    '• Ich arbeite bei …\n• Meine Aufgaben sind …\n• Besonders gut gefällt mir …\n• In Zukunft möchte ich …\n• Wo arbeitest du?',
    '• Ich arbeite bei … (Я работаю в ...)\n• Meine Aufgaben sind … (Мои обязанности ...)\n• Besonders gut gefällt mir … (Мне особенно нравится ...)\n• In Zukunft möchte ich … (В будущем я хочу ...)\n• Wo arbeitest du? (Где ты работаешь?)',
    '• Ich arbeite bei … (I work at ...)\n• Meine Aufgaben sind … (My tasks are ...)\n• Besonders gut gefällt mir … (I especially like ...)\n• In Zukunft möchte ich … (In the future, I want to ...)\n• Wo arbeitest du? (Where do you work?)',
    TRUE
),
(
    UTC_TIMESTAMP(),
    'Technologie und soziale Medien',
    'Технологии и социальные сети',
    'Technology and Social Media',
    'Sie haben sich eine neue technische Geräte oder App gekauft. Schreiben Sie eine E-Mail an einen Freund oder eine Freundin und berichten Sie darüber. Gehen Sie dabei auf folgende Punkte ein:\n\n- Was haben Sie gekauft oder heruntergeladen?\n- Warum haben Sie sich dafür entschieden?\n- Welche Vorteile oder Nachteile hat es?\n- Würden Sie es weiterempfehlen?',
    'Вы купили новое техническое устройство или скачали приложение. Напишите письмо другу или подруге и расскажите об этом. Затроньте следующие моменты:\n\n- Что вы купили или скачали?\n- Почему вы решили это сделать?\n- Какие у этого преимущества или недостатки?\n- Порекомендовали бы вы это другим?',
    'You have bought a new tech device or downloaded an app. Write an email to a friend and tell them about it. Address the following points:\n\n- What did you buy or download?\n- Why did you decide on it?\n- What are its advantages or disadvantages?\n- Would you recommend it?',
    '• Ich habe mir … gekauft/heruntergeladen\n• Ich habe mich dafür entschieden, weil …\n• Besonders gut/schlecht finde ich …\n• Ich empfehle es, weil …\n• Was benutzt du am liebsten?',
    '• Ich habe mir … gekauft/heruntergeladen (Я купил/скачал ...)\n• Ich habe mich dafür entschieden, weil … (Я выбрал это, потому что ...)\n• Besonders gut/schlecht finde ich … (Мне особенно нравится/не нравится ...)\n• Ich empfehle es, weil … (Я рекомендую это, потому что ...)\n• Was benutzt du am liebsten? (Что ты используешь больше всего?)',
    '• Ich habe mir … gekauft/heruntergeladen (I bought/downloaded ...)\n• Ich habe mich dafür entschieden, weil … (I chose it because ...)\n• Besonders gut/schlecht finde ich … (I find it especially good/bad ...)\n• Ich empfehle es, weil … (I recommend it because ...)\n• Was benutzt du am liebsten? (What do you use the most?)',
    TRUE
),
(
    UTC_TIMESTAMP(),
    'Bücher und Lesen',
    'Книги и чтение',
    'Books and Reading',
    'Sie haben ein interessantes Buch gelesen. Schreiben Sie eine E-Mail an einen Freund oder eine Freundin und berichten Sie darüber. Gehen Sie dabei auf folgende Punkte ein:\n\n- Welches Buch haben Sie gelesen?\n- Worum geht es in dem Buch?\n- Was hat Ihnen besonders gefallen?\n- Würden Sie es weiterempfehlen?',
    'Вы прочитали интересную книгу. Напишите письмо другу или подруге и расскажите об этом. Затроньте следующие моменты:\n\n- Какую книгу вы прочитали?\n- О чем эта книга?\n- Что вам особенно понравилось?\n- Порекомендовали бы вы ее другим?',
    'You have read an interesting book. Write an email to a friend and tell them about it. Address the following points:\n\n- What book did you read?\n- What is the book about?\n- What did you like the most?\n- Would you recommend it?',
    '• Ich habe das Buch … gelesen\n• Es geht um …\n• Besonders spannend fand ich …\n• Ich empfehle dieses Buch, weil …\n• Was liest du gerne?',
    '• Ich habe das Buch … gelesen (Я прочитал книгу ...)\n• Es geht um … (В ней речь идет о ...)\n• Besonders spannend fand ich … (Особенно захватывающим я нашел ...)\n• Ich empfehle dieses Buch, weil … (Я рекомендую эту книгу, потому что ...)\n• Was liest du gerne? (Что ты любишь читать?)',
    '• Ich habe das Buch … gelesen (I read the book ...)\n• Es geht um … (It is about ...)\n• Besonders spannend fand ich … (I found it especially exciting ...)\n• Ich empfehle dieses Buch, weil … (I recommend this book because ...)\n• Was liest du gerne? (What do you like to read?)',
    TRUE
),
(
    UTC_TIMESTAMP(),
    'Mein Lieblingscomputerspiel',
    'Моя любимая компьютерная игра',
    'My Favorite Computer Game',
    'Sie haben ein Lieblingscomputerspiel, das Sie gerne spielen. Schreiben Sie eine E-Mail an einen Freund oder eine Freundin und berichten Sie darüber. Gehen Sie dabei auf folgende Punkte ein:\n\n- Wie heißt das Spiel?\n- Warum gefällt es Ihnen?\n- Wie oft spielen Sie es?\n- Würden Sie es weiterempfehlen?',
    'У вас есть любимая компьютерная игра, в которую вы любите играть. Напишите письмо другу или подруге и расскажите об этом. Затроньте следующие моменты:\n\n- Как называется эта игра?\n- Почему она вам нравится?\n- Как часто вы в нее играете?\n- Порекомендовали бы вы ее другим?',
    'You have a favorite computer game that you love to play. Write an email to a friend and tell them about it. Address the following points:\n\n- What is the name of the game?\n- Why do you like it?\n- How often do you play it?\n- Would you recommend it?',
    '• Mein Lieblingsspiel ist …\n• Besonders mag ich …\n• Ich spiele es … Mal pro Woche\n• Ich empfehle es, weil …\n• Spielst du auch Computerspiele?',
    '• Mein Lieblingsspiel ist … (Моя любимая игра - ...)\n• Besonders mag ich … (Мне особенно нравится ...)\n• Ich spiele es … Mal pro Woche (Я играю в нее ... раз в неделю)\n• Ich empfehle es, weil … (Я рекомендую ее, потому что ...)\n• Spielst du auch Computerspiele? (Ты тоже играешь в компьютерные игры?)',
    '• Mein Lieblingsspiel ist … (My favorite game is ...)\n• Besonders mag ich … (I especially like ...)\n• Ich spiele es … Mal pro Woche (I play it ... times per week)\n• Ich empfehle es, weil … (I recommend it because ...)\n• Spielst du auch Computerspiele? (Do you also play computer games?)',
    TRUE
),
(
    UTC_TIMESTAMP(),
    'Mein Lieblingsrestaurant in meiner Stadt',
    'Мой любимый ресторан в моем городе',
    'My Favorite Restaurant in My City',
    'Sie haben ein Lieblingsrestaurant in Ihrer Stadt, das Sie gerne besuchen. Schreiben Sie eine E-Mail an einen Freund oder eine Freundin und berichten Sie darüber. Gehen Sie dabei auf folgende Punkte ein:\n\n- Wie heißt das Restaurant?\n- Was gibt es dort zu essen?\n- Warum mögen Sie es besonders?\n- Würden Sie es weiterempfehlen?',
    'У вас есть любимый ресторан в вашем городе, куда вы любите ходить. Напишите письмо другу или подруге и расскажите об этом. Затроньте следующие моменты:\n\n- Как называется этот ресторан?\n- Какие блюда там подают?\n- Почему он вам особенно нравится?\n- Порекомендовали бы вы его другим?',
    'You have a favorite restaurant in your city that you love to visit. Write an email to a friend and tell them about it. Address the following points:\n\n- What is the name of the restaurant?\n- What kind of food do they serve?\n- Why do you like it?\n- Would you recommend it?',
    '• Mein Lieblingsrestaurant heißt …\n• Dort gibt es … zu essen\n• Besonders gut gefällt mir …\n• Ich empfehle es, weil …\n• Welches Restaurant magst du?',
    '• Mein Lieblingsrestaurant heißt … (Мой любимый ресторан называется ...)\n• Dort gibt es … zu essen (Там подают ...)\n• Besonders gut gefällt mir … (Мне особенно нравится ...)\n• Ich empfehle es, weil … (Я рекомендую его, потому что ...)\n• Welches Restaurant magst du? (Какой ресторан тебе нравится?)',
    '• Mein Lieblingsrestaurant heißt … (My favorite restaurant is called ...)\n• Dort gibt es … zu essen (They serve ... there)\n• Besonders gut gefällt mir … (I especially like ...)\n• Ich empfehle es, weil … (I recommend it because ...)\n• Welches Restaurant magst du? (Which restaurant do you like?)',
    TRUE
),
(
    UTC_TIMESTAMP(),
    'Auswandern nach Deutschland',
    'Иммиграция в Германию',
    'Immigration to Germany',
    'Sie oder eine Person, die Sie kennen, sind nach Deutschland umgezogen. Schreiben Sie eine E-Mail an einen Freund oder eine Freundin und berichten Sie darüber. Gehen Sie dabei auf folgende Punkte ein:\n\n- Wann und warum sind Sie oder die Person nach Deutschland gezogen?\n- Was waren die größten Herausforderungen?\n- Was gefällt Ihnen an Deutschland besonders?\n- Welche Tipps haben Sie für andere, die nach Deutschland ziehen möchten?',
    'Вы или кто-то, кого вы знаете, переехали в Германию. Напишите письмо другу или подруге и расскажите об этом. Затроньте следующие моменты:\n\n- Когда и почему вы или этот человек переехали в Германию?\n- Какие были самые большие трудности?\n- Что вам особенно нравится в Германии?\n- Какие советы у вас есть для тех, кто хочет переехать в Германию?',
    'You or someone you know has moved to Germany. Write an email to a friend and tell them about it. Address the following points:\n\n- When and why did you or this person move to Germany?\n- What were the biggest challenges?\n- What do you like most about Germany?\n- What tips do you have for others who want to move to Germany?',
    '• Ich bin nach Deutschland gezogen, weil …\n• Eine große Herausforderung war …\n• Besonders gefällt mir …\n• Mein Tipp für Auswanderer ist …\n• Könntest du dir vorstellen, nach Deutschland zu ziehen?',
    '• Ich bin nach Deutschland gezogen, weil … (Я переехал в Германию, потому что ...)\n• Eine große Herausforderung war … (Самой большой сложностью было ...)\n• Besonders gefällt mir … (Мне особенно нравится ...)\n• Mein Tipp für Auswanderer ist … (Мой совет для эмигрантов - ...)\n• Könntest du dir vorstellen, nach Deutschland zu ziehen? (Ты мог бы представить себе переезд в Германию?)',
    '• Ich bin nach Deutschland gezogen, weil … (I moved to Germany because ...)\n• Eine große Herausforderung war … (The biggest challenge was ...)\n• Besonders gefällt mir … (I especially like ...)\n• Mein Tipp für Auswanderer ist … (My tip for immigrants is ...)\n• Könntest du dir vorstellen, nach Deutschland zu ziehen? (Could you imagine moving to Germany?)',
    TRUE
),
(
    UTC_TIMESTAMP(),
    'Mein bester Freund / Meine beste Freundin',
    'Мой лучший друг / Моя лучшая подруга',
    'My Best Friend',
    'Schreiben Sie eine E-Mail an einen Freund oder eine Freundin und erzählen Sie von Ihrem besten Freund oder Ihrer besten Freundin. Gehen Sie dabei auf folgende Punkte ein:\n\n- Wie heißt Ihr bester Freund / Ihre beste Freundin?\n- Wie lange kennen Sie sich?\n- Was mögen Sie besonders an ihm/ihr?\n- Welche gemeinsamen Erlebnisse verbinden Sie?',
    'Напишите письмо другу или подруге и расскажите о своем лучшем друге или лучшей подруге. Затроньте следующие моменты:\n\n- Как зовут вашего лучшего друга / лучшую подругу?\n- Как давно вы знакомы?\n- Что вам особенно нравится в этом человеке?\n- Какие общие воспоминания у вас есть?',
    'Write an email to a friend and talk about your best friend. Address the following points:\n\n- What is your best friend’s name?\n- How long have you known each other?\n- What do you like most about them?\n- What shared experiences connect you?',
    '• Mein bester Freund / Meine beste Freundin heißt …\n• Wir kennen uns seit …\n• Besonders mag ich an ihm/ihr …\n• Unser schönstes gemeinsames Erlebnis war …\n• Hast du einen besten Freund / eine beste Freundin?',
    '• Mein bester Freund / Meine beste Freundin heißt … (Моего лучшего друга / Мою лучшую подругу зовут ...)\n• Wir kennen uns seit … (Мы знакомы с ...)\n• Besonders mag ich an ihm/ihr … (Мне особенно нравится в нем/ней ...)\n• Unser schönstes gemeinsames Erlebnis war … (Наше самое лучшее совместное воспоминание - ...)\n• Hast du einen besten Freund / eine beste Freundin? (У тебя есть лучший друг / лучшая подруга?)',
    '• Mein bester Freund / Meine beste Freundin heißt … (My best friend’s name is ...)\n• Wir kennen uns seit … (We have known each other since ...)\n• Besonders mag ich an ihm/ihr … (What I like most about them is ...)\n• Unser schönstes gemeinsames Erlebnis war … (Our best shared experience was ...)\n• Hast du einen besten Freund / eine beste Freundin? (Do you have a best friend?)',
    TRUE
),
(
    UTC_TIMESTAMP(),
    'Mein Leben während der Corona-Pandemie',
    'Моя жизнь во время пандемии COVID-19',
    'My Life During the COVID-19 Pandemic',
    'Schreiben Sie eine E-Mail an einen Freund oder eine Freundin und erzählen Sie, wie Sie die Zeit während der Corona-Pandemie verbracht haben. Gehen Sie dabei auf folgende Punkte ein:\n\n- Wie hat sich Ihr Alltag verändert?\n- Was haben Sie in dieser Zeit gelernt oder gemacht?\n- Welche Schwierigkeiten hatten Sie?\n- Gibt es etwas Positives, das Sie aus dieser Zeit mitnehmen?',
    'Напишите письмо другу или подруге и расскажите, как вы провели время во время пандемии COVID-19. Затроньте следующие моменты:\n\n- Как изменился ваш повседневный образ жизни?\n- Чему вы научились или чем занимались в это время?\n- С какими трудностями вы столкнулись?\n- Есть ли что-то положительное, что вы вынесли из этого времени?',
    'Write an email to a friend and talk about how you spent your time during the COVID-19 pandemic. Address the following points:\n\n- How did your daily life change?\n- What did you learn or do during this time?\n- What challenges did you face?\n- Is there something positive that you took away from this time?',
    '• Mein Alltag hat sich verändert, weil …\n• Während der Pandemie habe ich … gelernt / gemacht\n• Eine große Herausforderung war …\n• Positiv fand ich …\n• Wie hast du diese Zeit erlebt?',
    '• Mein Alltag hat sich verändert, weil … (Моя повседневная жизнь изменилась, потому что ...)\n• Während der Pandemie habe ich … gelernt / gemacht (Во время пандемии я научился / занимался ...)\n• Eine große Herausforderung war … (Большой проблемой было ...)\n• Positiv fand ich … (Положительным моментом для меня было ...)\n• Wie hast du diese Zeit erlebt? (Как ты пережил это время?)',
    '• Mein Alltag hat sich verändert, weil … (My daily life changed because ...)\n• Während der Pandemie habe ich … gelernt / gemacht (During the pandemic, I learned / did ...)\n• Eine große Herausforderung war … (A big challenge was ...)\n• Positiv fand ich … (I found it positive that ...)\n• Wie hast du diese Zeit erlebt? (How did you experience this time?)',
    TRUE
),
(
    UTC_TIMESTAMP(),
    'Einem Freund bei der Berufswahl helfen',
    'Помощь другу в выборе профессии',
    'Helping a Friend Choose a Career',
    'Ihr Freund oder Ihre Freundin ist unsicher, welchen Beruf er oder sie wählen soll. Schreiben Sie eine E-Mail und geben Sie ihm oder ihr Ratschläge. Gehen Sie dabei auf folgende Punkte ein:\n\n- Was sind die Stärken Ihres Freundes/Ihrer Freundin?\n- Welche Berufe passen zu diesen Stärken?\n- Welche Vorteile hat der gewählte Beruf?\n- Wie kann er oder sie sich darauf vorbereiten?',
    'Ваш друг или подруга не уверен(а), какую профессию выбрать. Напишите письмо и дайте ему или ей совет. Затроньте следующие моменты:\n\n- Какие сильные стороны у вашего друга/подруги?\n- Какие профессии подходят под эти качества?\n- Какие преимущества есть у выбранной профессии?\n- Как он или она может подготовиться к этому?',
    'Your friend is unsure about which career to choose. Write an email and give him or her advice. Address the following points:\n\n- What are your friend’s strengths?\n- Which careers match these strengths?\n- What advantages does the chosen profession have?\n- How can he or she prepare for it?',
    '• Ich denke, du bist besonders gut in …\n• Ein Beruf, der dazu passt, ist …\n• Besonders vorteilhaft ist …\n• Um dich vorzubereiten, kannst du …\n• Hast du schon eine Idee, welchen Beruf du wählen möchtest?',
    '• Ich denke, du bist besonders gut in … (Я думаю, ты особенно хорош в ...)\n• Ein Beruf, der dazu passt, ist … (Профессия, которая подходит для этого - ...)\n• Besonders vorteilhaft ist … (Особенно выгодно в этой профессии ...)\n• Um dich vorzubereiten, kannst du … (Чтобы подготовиться, ты можешь ...)\n• Hast du schon eine Idee, welchen Beruf du wählen möchtest? (У тебя уже есть идея, какую профессию выбрать?)',
    '• Ich denke, du bist besonders gut in … (I think you are especially good at ...)\n• Ein Beruf, der dazu passt, ist … (A career that fits this is ...)\n• Besonders vorteilhaft ist … (A big advantage is ...)\n• Um dich vorzubereiten, kannst du … (To prepare, you can ...)\n• Hast du schon eine Idee, welchen Beruf du wählen möchtest? (Do you already have an idea of which career to choose?)',
    TRUE
);


