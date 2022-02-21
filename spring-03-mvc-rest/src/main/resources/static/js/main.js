var bookApi = Vue.resource('/library/book{/id}')
var genreApi = Vue.resource('/library/genre{/id}')
var authorApi = Vue.resource('/library/author')


Vue.component('book-form', {
    props: ['books', 'genres', 'authors'],
    data: function (){
        return {
            name: '',
            genre: null,
            authorsList: [],
        }
    },
    template:
        '<div>' +
            '<div className="row">' +
                '<label for="name">Name:</label>' +
                '<input name="name" type="text" placeholder="Write book-name" v-model="name"/>' +
            '</div>' +
            '<div className="row">' +
                '<label for="genres">Genre:</label>' +
                '<select class="form-control" id="genres" v-model="genre">' +
                    '<option v-for="genre in genres" v-bind:value="genre">{{ genre.name }}</option>' +
                '</select>' +
            '</div>' +
            '<div className="row">' +
                '<table>' +
                    '<thead>' +
                    '<tr>' +
                        '<th>Author</th>' +
                    '</tr>' +
                    '</thead>' +
                    '<tbody>' +
                        '<tr v-for="author in authors" >' +
                            '<td><input type="checkbox" v-bind:value="author" v-model="authorsList"/></td>' +
                            '<td>{{ author.name }}</td>' +
                        '</tr>' +
                    '</tbody>' +
                '</table>' +
            '</div>' +
            '<input name="save-btn" type="button" value="Save" @click="save"/>' +
        '</div>',
    methods: {
        save: function () {
            var book = { name: this.name, genre: this.genre, authors: this.authorsList };
            bookApi.save({}, book).then(result =>
                result.json().then(data => {
                    this.books.push(data);
                })
            )
            this.name = '';
            this.genre = null;
            this.authorsList = [];
        }
    }
})

Vue.component('authors-row', {
    props: ['authors'],
    template:
        '<table class="authors">' +
            '<tr v-for="author in authors">' +
                '<td>{{ author.name }}</td>' +
            '</tr>' +
        '</table>'
})

Vue.component('book-row', {
    props: ['book', 'books'],
    template:
        '<tr>' +
            '<td>{{ book.id }}</td>' +
            '<td>{{ book.name }}</td>' +
            '<td>{{ book.genre.name }}</td>' +
            '<td><authors-row :authors="book.authors"/></td>' +
            '<td><button type="submit" class="remover" @click="del">' +
                    '<img src="../images/delete.png" alt=""/>' +
                '</button>' +
            '</td>' +
        '</tr>',
    methods: {
        del: function () {
            bookApi.remove({id: this.book.id}).then(result => {
                if (result.ok) {
                    this.books.splice(this.books.indexOf(this.book), 1)
                }
            })
        }
    }
})

Vue.component('books-list', {
    props: ['books', 'genres', 'authors'],
    template:
        '<div>' +
            '<table class="book-table">' +
                '<thead>' +
                    '<tr>' +
                        '<th>ID</th>' +
                        '<th>Name</th>' +
                        '<th>Genre</th>' +
                        '<th>Authors</th>' +
                        '<th>Delete</th>' +
                    '</tr>' +
                '</thead>' +
                '<tbody>' +
                    '<book-row v-for="book in books" :key="book.id" :book="book" :books="books"/>' +
                ' </tbody>' +
            '</table>' +
            '<book-form :genres="genres" :authors="authors" :books="books"/>' +
        '</div>',

    created: function () {
        bookApi.get().then(result =>
            result.json().then(data =>
                data.forEach(book => this.books.push(book))
            )
        );
        genreApi.get().then(result =>
            result.json().then(data =>
                data.forEach(genre => this.genres.push(genre))
            )
        );
        authorApi.get().then(result =>
            result.json().then(data =>
                data.forEach(author => this.authors.push(author))
            )
        )
    }
})



var app = new Vue({
    el: '#app',
    template: '<books-list :books="books" :genres="genres" :authors="authors"/>',
    data: {
        books: [],
        genres: [],
        authors: []
    }
});
