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
            <h1>Update Hardware Inventory</h1>
            <div class="row">
                <div class="col-lg-8">
                    <div class="main-box">
                        <h2>Details</h2>
                        <form id="update_hardwareinventory_form" class="form-horizontal" role="form" action="${contextPath}/wh/whInventory/update" method="post">
                            <input type="hidden" name="refId" value="${whInventory.refId}" />
                            <div class="form-group">
                                <label for="materialPassNo" class="col-lg-3 control-label">Material Pass No.</label>
                                <div class="col-lg-8">
                                    <input type="text" class="form-control" id="materialPassNo" name="materialPassNo" value="${whInventory.materialPassNo}" readonly>
                                </div>
                            </div>
                                <div class="form-group">
                                <label for="materialPassExpiry" class="col-lg-3 control-label">Material Pass Expiry Date</label>
                                <div class="col-lg-8">
                                    <input type="text" class="form-control" id="materialPassExpiry" name="materialPassExpiry" value="${whInventory.materialPassExpiry}" readonly>
                                </div>
                            </div>
                            <div class="form-group">
                                <label for="equipmentType" class="col-lg-3 control-label">Hardware Category</label>
                                <div class="col-lg-8">
                                    <input type="text" class="form-control" id="equipmentType" name="equipmentType" value="${whInventory.equipmentType}" readonly>
                                </div>
                            </div>
                            <div class="form-group">
                                <label for="equipmentId" class="col-lg-3 control-label">Hardware ID</label>
                                <div class="col-lg-8">
                                    <input type="text" class="form-control" id="equipmentId" name="equipmentId" value="${whInventory.equipmentId}" readonly>
                                </div>
                            </div>
                            <div class="form-group" id="quantitydiv" hidden>
                                <label for="quantity" class="col-lg-3 control-label">Quantity</label>
                                <div class="col-lg-2">
                                    <input type="text" class="form-control" id="quantity" name="quantity" placeholder="Quantity" value="${whInventory.quantity}" readonly>
                                </div>
                            </div>
                            <div class="form-group">
                                <label for="inventoryRack" class="col-lg-3 control-label">Inventory Rack</label>
                                <div class="col-lg-8">
                                    <input type="text" class="form-control" id="inventoryRack" name="inventoryRack" placeholder="${whInventory.inventoryRack}" required>
                                </div>
                            </div>
                            <div class="form-group">
                                <label for="inventoryShelf" class="col-lg-3 control-label">Inventory Shelf</label>
                                <div class="col-lg-8">
                                    <input type="text" class="form-control" id="inventoryShelf" name="inventoryShelf" placeholder="${whInventory.inventoryShelf}" required>
                                </div>
                            </div>
                            <div id = "alert_placeholder2"></div>
                            <a href="${contextPath}/wh/whInventory" class="btn btn-info pull-left"><i class="fa fa-reply"></i> Back</a>
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
                //temporary disabled
                $('#inventoryRack').bind('copy paste cut', function (e)  {
                    e.preventDefault(); //this line will help us to disable cut,copy,paste  
                });
                
                $('#inventoryShelf').bind('copy paste cut', function (e)  {
                    e.preventDefault(); //this line will help us to disable cut,copy,paste  
                });
                
                var element = $('#equipmentType');
                if (element.val() === "Motherboard") {
                    $("#quantitydiv").hide();
                } else if (element.val() === "Stencil") {
                    $("#quantitydiv").hide();
                } else if (element.val() === "Tray") {
                    $("#quantitydiv").show();
                } else if (element.val() === "PCB") {
                    $("#quantitydiv").show();
                } else {
                    $("#quantitydiv").hide();
                }
                
                bootstrap_alert = function () {};
                bootstrap_alert.warning = function (message) {
                    $('#alert_placeholder2').html('<div class="alert alert-danger alert-dismissable"><button type="button" class="close" data-dismiss="alert" aria-hidden="true">&times;</button><span>' + message + '</span></div>')
                };
                if ($('#inventoryRack').val() !== $('#inventoryShelf').val().substring(0,6) || $('#inventoryRack').val().length !== 6 && $('#inventoryShelf').val().length !== 10 
                    && ($('#inventoryRack').val() !== "" && $('#inventoryShelf').val() !== "")) {
                    bootstrap_alert.warning('Inventory assigned is NOT VALID! Please re-check and try again.');
                    $("#inventoryRack").addClass('highlight');
                    $("#inventoryShelf").addClass('highlight');
                } else {
                    if ($('#equipmentType').val() === 'Motherboard' && ($('#inventoryRack').val() !== "" && $('#inventoryShelf').val() !== "")) {
                        if($('#inventoryRack').val().substring(0,4) === "S-SY" || $('#inventoryRack').val().substring(0,4) === "S-AC" || $('#inventoryRack').val().substring(0,4) === "S-WF" || $('#inventoryRack').val().substring(0,4) !== "S-IO"
                        || $('#inventoryRack').val().substring(0,4) === "S-BB" || $('#inventoryRack').val().substring(0,4) === "S-HA" || $('#inventoryRack').val().substring(0,4) === "S-PT") {
                            //do nothing
                        } else {
                            bootstrap_alert.warning('Inventory assigned is NOT VALID! Please re-check and try again.');
                            //$("#hardwareBarcode2").effect("highlight", {}, 1000);
                            $("#inventoryRack").addClass('highlight');
                            $("#inventoryShelf").addClass('highlight');
                        }
                    } else if ($('#equipmentType').val() === 'Stencil' && ($('#inventoryRack').val() !== "" && $('#inventoryShelf').val() !== "")) {
                        if($('#inventoryRack').val().substring(0,4) === "S-ST") {
                            //do nothing
                        } else {
                            bootstrap_alert.warning('Inventory assigned is NOT VALID! Please re-check and try again.');
                            //$("#hardwareBarcode2").effect("highlight", {}, 1000);
                            $("#inventoryRack").addClass('highlight');
                            $("#inventoryShelf").addClass('highlight');
                        }
                    } else if ($('#equipmentType').val() === 'Tray' && ($('#inventoryRack').val() !== "" && $('#inventoryShelf').val() !== "")) {
                        if($('#inventoryRack').val().substring(0,4) === "S-TJ" || $('#inventoryRack').val().substring(0,4) === "S-TR") {
                            //do nothing
                        } else {
                            bootstrap_alert.warning('Inventory assigned is NOT VALID! Please re-check and try again.');
                            //$("#hardwareBarcode2").effect("highlight", {}, 1000);
                            $("#inventoryRack").addClass('highlight');
                            $("#inventoryShelf").addClass('highlight');
                        }
                    } else if ($('#equipmentType').val() === 'PCB' && ($('#inventoryRack').val() !== "" && $('#inventoryShelf').val() !== "")) {
                        if($('#inventoryRack').val().substring(0,4) === "S-PC") {
                            //do nothing
                        } else {
                            bootstrap_alert.warning('Inventory assigned is NOT VALID! Please re-check and try again.');
                            //$("#hardwareBarcode2").effect("highlight", {}, 1000);
                            $("#inventoryRack").addClass('highlight');
                            $("#inventoryShelf").addClass('highlight');
                        }
                    }
                }
                
                var validator = $("#update_hardwareinventory_form").validate({
                    rules: {
                        inventoryRack: {
                            required: true,
                            minlength: 6,
                            maxlength: 6
                        },
                        inventoryShelf: {
                            required: true,
                            minlength: 10,
                            maxlength: 10
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