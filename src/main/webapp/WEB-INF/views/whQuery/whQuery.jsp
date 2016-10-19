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
                <div class="col-lg-12">
                    <div class="main-box">
                        <form id="update_hardwareinventory_form" class="form-horizontal" role="form" action="${contextPath}/wh/whQuery" method="post" style="width: 100%">
                            <div class="form-group col-lg-12" >
                                <label for="materialPassNo" class="col-lg-2 control-label">Material Pass No.</label>
                                <div class="col-lg-4">
                                    <input type="text" class="form-control" id="materialPassNo" name="materialPassNo" value="">
                                </div>
                                <!--<div class="col-lg-2"></div>-->
                                <label for="materialPassExpiry" class="col-lg-2 control-label">Material Pass Expiry Date</label>
                                <div class="col-lg-4">
                                    <input type="text" class="form-control" id="materialPassExpiry" name="materialPassExpiry" value="">
                                </div>
                            </div>
                            
                            <div class="form-group col-lg-12">
                                <label for="equipmentType" class="col-lg-2 control-label">Hardware Category</label>
                                <div class="col-lg-4">
                                    <input type="text" class="form-control" id="equipmentType" name="equipmentType" value="" >
                                </div>
                                <!--<div class="col-lg-2"></div>-->
                                <label for="equipmentId" class="col-lg-2 control-label">Hardware ID</label>
                                <div class="col-lg-4">
                                    <input type="text" class="form-control" id="equipmentId" name="equipmentId" value="" >
                                </div>
                            </div>
                            
                            <div class="form-group col-lg-12">
                                <label for="requestedBy" class="col-lg-2 control-label">Requested By</label>
                                <div class="col-lg-4">
                                    <input type="text" class="form-control" id="requestedBy" name="requestedBy" value="">
                                </div>
                                <!--<div class="col-lg-2"></div>-->
                                <label for="requestedDate" class="col-lg-2 control-label">Requested Date</label>
                                <div class="col-lg-4">
                                    <input type="text" class="form-control" id="requestedDate" name="requestedDate" value="">
                                </div>
                            </div>
                            
                            <div class="form-group col-lg-12">
                                <label for="inventoryRack" class="col-lg-2 control-label">Inventory Rack</label>
                                <div class="col-lg-4">
                                    <input type="text" class="form-control" id="inventoryRack" name="inventoryRack">
                                </div>
                                <!--<div class="col-lg-2"></div>-->
                                <label for="inventoryShelf" class="col-lg-2 control-label">Inventory Shelf</label>
                                <div class="col-lg-4">
                                    <input type="text" class="form-control" id="inventoryShelf" name="inventoryShelf">
                                </div>
                            </div>
                            
                            <div class="form-group col-lg-12">
                                <label for="fromTable" class="col-lg-2 control-label">Module</label>
                                <div class="col-lg-4">
                                    <input type="checkbox" name="fromTable" value="Retrieve"> Retrieve 
                                    <input type="checkbox" name="fromTable" value="Inventory"> Inventory 
                                    <input type="checkbox" name="fromTable" value="Request"> Request 
                                    <input type="checkbox" name="fromTable" value="Shipping"> Shipping 
                                </div>
<!--                                <div class="col-lg-2"></div>-->
                                <label for="whereRelation" class="col-lg-2 control-label">WHERE Clause Relation</label>
                                <div class="col-lg-4">
                                    <select id="whereRelation" name="whereRelation" class="js-example-basic-single" style="width: 100%">
                                                <option value="0" selected=></option>
                                                <option value="1">AND</option>
                                                <option value="2">OR</option>
                                                <option value="3">NOT</option>
                                    </select>
                                </div>
                            </div>
                            <div class="col-lg-12"></div>
                            <div class="col-lg-12">
                                <a href="/" class="btn btn-info pull-left" id="cancel"><i class="fa fa-reply"></i>Back</a>
                                <button type="submit" class="btn btn-primary pull-right" name="submit" id="submit" >View Data</button>
                            </div>
                            <div class="clearfix"></div>
                        </form>
                    </div>
                </div>
                <div class="col-lg-12">
                    <table id="data">
                        <tr>
                            <td>A</td>
                            <td>A</td>
                            <td>A</td>
                        </tr>
                    </table>
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
                var validator = $("#update_hardwareinventory_form").validate({
                    //do nothing yet
                });
                
                $(".cancel").click(function () {
                    validator.resetForm();
                });
                
                $(".submit").click(function () {
                    $("#data").show();
                });
            });
        </script>
    </s:layout-component>
</s:layout-render>