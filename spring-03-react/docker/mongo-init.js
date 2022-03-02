print('Start #################################################################');

db = db.getSiblingDB("library");

db.authors.drop()
db.genres.drop()
db.books.drop()

db.createCollection('authors');
db.createCollection('genres');
db.createCollection('books');

db.authors.insertMany([
    { "_id": '1_a', name : 'Толкин, Джон Рональд Руэл'},
    { "_id": '2_a', name : 'Пушкин Александр Сергеевич'},
    { "_id": '3_a', name : 'Стругацкий Аркадий'},
    { "_id": '4_a', name : 'Стругацкий Илья'}
]);

db.genres.insertMany([
    { "_id": '1_g', name : 'роман-эпопея'},
    { "_id": '2_g', name : 'сказка'},
    { "_id": '3_g',name : 'Научная фантастика'}
]);

db.books.insertMany([
    { name : 'Хоббит', genre : '1_g', authors: [ '1_a' ]},
    { name : 'Властелин колец', genre : '1_g', authors: [ '1_a' ]},
    { name : 'Сказка о царе Салтане', genre : '2_g', authors: [ '2_a' ]},
    { name : 'За миллиард лет до конца света.(сборник)', genre : '3_g', authors: [ '3_a', '4_a' ]},

]);

print('END #################################################################');
