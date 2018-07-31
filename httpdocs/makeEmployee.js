    //on click function for making the employee
    $(function() {
        var $employeeName = $('#employee_name');
        var $employeeEmail = $('#employee_email_1');
        var $employeePhone = $('#employee_phone');
        var $jobType = $('employee_jobType');
        var $password = $('passWord');
        var $StoreID = $('StoreID');
        
        $('#makeEmployeeButton').on('click'), function () {
            
            var payLoad = {
                name: employeeName.val(),
                email: employeeEmail.val(),
                phone: employeePhone.val(),
                job: jobType.val(),
                password: passWord.val(),
                StoreId : StoreID.val(),
            };

            $.ajax({
                type: 'POST',
                url: 'http://knightfinder.com/WEBAPI/CreateEmployee.aspx',
                data: payLoad,
                success: function(newEmployee) {
                    alert("Employee :"+ newEmployee.$employeeName + "successfully Added to database")

                },
                error: function() {
                    alert("error saving employee \n error code: "+ error);
                }
            });
    
    
    };

});