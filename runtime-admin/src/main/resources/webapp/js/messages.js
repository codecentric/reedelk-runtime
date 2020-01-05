let Messages = (function () {
    return {
        Success: function (message) {
            toastr.success(message);
        },

        Error: function (message) {
            toastr.error(message);
        }
    }
})();