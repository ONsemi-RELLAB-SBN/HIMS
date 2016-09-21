<%@page contentType="text/html;charset=UTF-8" %>
<%@include file="/WEB-INF/base/taglibs.jsp" %>
<s:layout-render name="/WEB-INF/base/base.jsp">
    <s:layout-component name="page_css">
        <link rel="stylesheet" href="${contextPath}/resources/private/css/libs/datepicker.css" type="text/css" />
    </s:layout-component>
    <s:layout-component name="page_css_inline">

    </s:layout-component>
    <s:layout-component name="page_container">
        <div class="col-lg-12">
            <h1>Query Search</h1>
            <div class="row">
                <div class="col-lg-6">
                    <div class="main-box">
                        <form id="update_hardwareinventory_form" class="form-horizontal" role="form" action="${contextPath}/wh/whQuery/edit" method="post">
                            <input type="hidden" name="refId" value="" />
                            <div class="form-group">
                                <label for="materialPassNo" class="col-lg-4 control-label">Material Pass No.</label>
                                <div class="col-lg-8">
                                    <input type="text" class="form-control" id="materialPassNo" name="materialPassNo" value="">
                                </div>
                            </div>
                                <div class="form-group">
                                <label for="materialPassExpiry" class="col-lg-4 control-label">Material Pass Expiry Date</label>
                                <div class="col-lg-8">
                                    <input type="text" class="form-control" id="materialPassExpiry" name="materialPassExpiry" value="">
                                </div>
                            </div>
                            <div class="form-group">
                                <label for="equipmentType" class="col-lg-4 control-label">Hardware Category</label>
                                <div class="col-lg-8">
                                    <input type="text" class="form-control" id="equipmentType" name="equipmentType" value="" >
                                </div>
                            </div>
                            <div class="form-group">
                                <label for="equipmentId" class="col-lg-4 control-label">Equipment ID</label>
                                <div class="col-lg-8">
                                    <input type="text" class="form-control" id="equipmentId" name="equipmentId" value="" >
                                </div>
                            </div>  
                            <div class="form-group" id="quantitydiv" hidden>
                                <label for="quantity" class="col-lg-4 control-label">Quantity</label>
                                <div class="col-lg-2">
                                    <input type="text" class="form-control" id="quantity" name="quantity" placeholder="Quantity" value="">
                                </div>
                            </div>
                            <div class="form-group">
                                <label for="inventoryLoc" class="col-lg-4 control-label">Inventory Location</label>
                                <div class="col-lg-8">
                                    <input type="text" class="form-control" id="inventoryLoc" name="inventoryLoc" placeholder="" >
                                </div>
                            </div>
                            <!--a href="${contextPath}/wh/whQuery" class="btn btn-info pull-left"><i class="fa fa-reply"></i>Back</a-->
                            <div class="pull-right">
                                <button type="reset" class="btn btn-secondary cancel">Reset</button>
                                <button type="submit" class="btn btn-primary">Save</button>
                            </div>
                            <div class="clearfix"></div>
                        </form>
                    </div>
                </div>	
            </div>
        </div>
    </s:layout-component>
    <s:layout-component name="page_js">
        <script src="${contextPath}/resources/validation/jquery.validate.min.js"></script>
        <script src="${contextPath}/resources/validation/additional-methods.js"></script>
        <script src="${contextPath}/resources/private/js/bootstrap-datepicker.js"></script>
    </s:layout-component>
    <s:layout-component name="page_js_inline">
        <script>
            $(document).ready(function () {

                var element = $('#equipmentType');
                if (element.val() === "Motherboard") {
                    $("#typediv").hide();
                    $("#quantitydiv").hide();
                } else if (element.val() === "Stencil") {
                    $("#typediv").hide();
                    $("#quantitydiv").hide();
                } else if (element.val() === "Tray") {
                    $("#quantitydiv").show();
                    $("#typediv").hide();
                } else if (element.val() === "PCB") {
                    $("#typediv").hide();
                    $("#quantitydiv").show();
                } else {
                    $("#typediv").hide();
                    $("#quantitydiv").hide();
                }
                
                var validator = $("#update_hardwareinventory_form").validate({
                    rules: {
                        inventoryLoc: {
                            required: true
                        }
                    }
                });
                
                $(".cancel").click(function () {
                    validator.resetForm();
                });  
                
                
                
            });
        </script>
    </s:layout-component>
</s:layout-render>