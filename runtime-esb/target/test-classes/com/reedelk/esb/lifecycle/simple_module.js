var TestModule = {};

(function() {

    this.function1 = function() {
        return 'function1';
    };

    this.function2 = function() {
        return 'function2';
    };

}).call(TestModule);