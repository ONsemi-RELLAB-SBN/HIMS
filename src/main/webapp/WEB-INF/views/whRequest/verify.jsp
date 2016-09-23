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
            <h1>Verification for Hardware Request</h1>
            <div class="row">
                <div class="col-lg-8">
                    <div class="main-box">
                        <h2>Details</h2>
                        <form id="approval_hardwarequest_form" class="form-horizontal" role="form" action="${contextPath}/wh/whRequest/approvalupdate" method="post">
                            <input type="hidden" name="refId" value="${whRequest.refId}" />
                            <div class="form-group">
                                <label for="equipmentType" class="col-lg-3 control-label">Hardware Type</label>
                                <div class="col-lg-8">
                                    <input type="text" class="form-control" id="equipmentType" name="equipmentType" value="${whRequest.equipmentType}" readonly>
                                </div>
                            </div>
                            <div class="form-group">
                                <label for="equipmentId" class="col-lg-3 control-label">Hardware ID</label>
                                <div class="col-lg-8">
                                    <input type="text" class="form-control" id="equipmentId" name="equipmentId" value="${whRequest.equipmentId}" readonly>
                                </div>
                            </div>  
                            <div class="form-group" id="quantitydiv" hidden>
                                <label for="quantity" class="col-lg-3 control-label">Quantity *</label>
                                <div class="col-lg-3">
                                    <input type="text" class="form-control" id="quantity" name="quantity" placeholder="Quantity" value="${whRequest.quantity}" readonly>
                                </div>
                            </div>
                            <div class="form-group">
                                <label for="materialPassNo" class="col-lg-3 control-label">Material Pass No.</label>
                                <div class="col-lg-8">
                                    <input type="text" class="form-control" id="materialPassNo" name="materialPassNo" value="${whRequest.materialPassNo}" readonly>
                                </div>
                            </div>
                            <div class="form-group">
                                <label for="materialPassExpiry" class="col-lg-3 control-label">Material Pass Expiry Date</label>
                                <div class="col-lg-8">
                                    <input type="text" class="form-control" id="materialPassExpiry" name="materialPassExpiry" value="${whRequest.materialPassExpiry}" readonly>
                                </div>
                            </div>
                            <div class="form-group">
                                <label for="inventoryLoc" class="col-lg-3 control-label">Hardware Inventory</label>
                                <div class="col-lg-8">
                                    <input type="text" class="form-control" id="inventoryLoc" name="inventoryLoc" value="${whRequest.inventoryRack}, ${whRequest.inventoryShelf}" readonly>
                                </div>
                            </div>
                            <div class="form-group">
                                <label for="retrieveBy" class="col-lg-3 control-label">Requested By</label>
                                <div class="col-lg-8">
                                    <input type="text" class="form-control" id="retrieveBy" name="retrieveBy" placeholder="Request For" value="${whRequest.requestedBy}" readonly>
                                </div>
                            </div>
                            <div class="form-group">
                                <label for="retrieveDate" class="col-lg-3 control-label">Requested Date</label>
                                <div class="col-lg-8">
                                    <input type="text" class="form-control" id="retrieveDate" name="retrieveDate" placeholder="Request For" value="${whRequest.requestedDate}" readonly>
                                </div>
                            </div>
                            <div class="form-group">
                                <label for="requestedEmail" class="col-lg-3 control-label">Requestor Email</label>
                                <div class="col-lg-8">
                                    <input type="text" class="form-control" id="requestedEmail" name="requestedEmail" placeholder="requestedEmail" value="${whRequest.requestedEmail}" readonly>
                                </div>
                            </div>
                            <div class="form-group">
                                <label for="requestedDate" class="col-lg-3 control-label">Requested Date</label>
                                <div class="col-lg-8">
                                    <input type="text" class="form-control" id="requestedDate" name="requestedDate" placeholder="Request For" value="${whRequest.requestedDate}" readonly>
                                </div>
                            </div>
                            
                            <div class="form-group">
                                <label for="remarks" class="col-lg-3 control-label">Remarks </label>
                                <div class="col-lg-8">
                                    <textarea class="form-control" rows="5" id="remarks" name="remarks" readonly>${whRequest.remarks}</textarea>
                                </div>
                            </div>
                            <div class="form-group">
                                <label for="status" class="col-lg-3 control-label">Status</label>
                                <div class="col-lg-8">
                                    <input type="text" class="form-control" id="status" name="status" value="${whRequest.status}" readonly>
                                </div>
                            </div>
                            <a href="${contextPath}/wh/whRequest" class="btn btn-info pull-left"><i class="fa fa-reply"></i>Back</a>
                            <div class="clearfix"></div>
                        </form>
                    </div>
                </div>	
            </div>
        </div>
                            
       <!--New Tab Menu-->
       <hr class="separator">
        <div class="col-lg-12">
            <br>
            <div class="row">
                <ul class="nav nav-tabs">
                    <li class="${mpActive}"><a data-toggle="tab" href="#mp">Verify Barcode Label</a></li>
                    <li class="${hiActive}"><a data-toggle="tab" href="#hi">Verify Hardware Location</a></li>
                </ul>
                <div class="tab-content">
                    <!--Tab for Scan Material Pass-->
                    <div id="mp" class="tab-pane fade ${mpActiveTab}">
                        <h6></h6>
                        <div class="col-lg-8">
                            <div class="main-box">
                                <h2>Verify Barcode Label</h2>
                                <form id="mp_form" class="form-horizontal" role="form" action="${contextPath}/wh/whRequest/verifyMp" method="post">
                                    <input type="hidden" name="refId" value="${whRequest.refId}" />
                                    <input type="hidden" name="status" value="${whRequest.status}" />
                                    <input type="hidden" id="materialPassNo" name="materialPassNo" value="${whRequest.materialPassNo}" />
                                    <input type="hidden" name="flag" value="${whRequest.flag}" />
                                    <input type="hidden" name="tab" value="${mpActiveTab}" />
                                    <div class="form-group">
                                        <label for=" barcodeVerify" class="col-lg-3 control-label">Scan Barcode Label</label>
                                        <div class="col-lg-8" id="barcodeVerifyDiv">
                                            <input type="text" class="form-control" id="barcodeVerify" name="barcodeVerify" autofocus="autofocus" required='true' value="${whRequest.barcodeVerify}"/>
                                        </div>
                                    </div>
                                    <div id = "alert_placeholder"></div>
                                    <br><br>
                                    <div class="pull-right">
                                        <button type="reset" class="btn btn-secondary cancel">Reset</button>
                                        <button type="submit" class="btn btn-primary" >Verify</button>
                                    </div>
                                    <div class="clearfix"></div>
                                </form>
                            </div>
                        </div>
                    </div>

                    <!--tab for inventory-->
                    <div id="hi" class="tab-pane fade ${hiActiveTab}">
                        <h6></h6>
                        <div class="col-lg-8">
                            <div class="main-box">
                                <h2>Hardware Location</h2>
                                <form id="hi_form" class="form-horizontal" role="form" action="${contextPath}/wh/whRequest/verifyInventory" method="post">
                                    <input type="hidden" name="refId" value="${whRequest.refId}" />
                                    <input type="hidden" name="materialPassNo" value="${whRequest.materialPassNo}" />
                                    <input type="hidden" name="status" value="${whRequest.status}" />
                                    <input type="hidden" name="flag" value="${whRequest.flag}" />
                                    <input type="hidden" id="inventoryRack" name="inventoryRack" value="${whRequest.inventoryRack}" />
                                    <input type="hidden" id="inventoryShelf" name="inventoryShelf" value="${whRequest.inventoryShelf}" />
                                    <input type="hidden" name="tab" value="${hiActiveTab}" />
                                    <div class="form-group">
                                        <label for=" inventoryRackVerify" class="col-lg-3 control-label">Scan Hardware's Rack Location</label>
                                        <div class="col-lg-8">
                                            <input type="text" class="form-control" id="inventoryRackVerify" name="inventoryRackVerify" autofocus="autofocus" value="${whRequest.inventoryRackVerify}" required/> 
                                        </div>
                                    </div>
                                    <div id = "alert_placeholder2"></div>
                                    <div class="form-group">                                        
                                        <label for=" inventoryShelfVerify" class="col-lg-3 control-label">Scan Hardware's Shelf Location</label>
                                        <div class="col-lg-8">
                                            <input type="text" class="form-control" id="inventoryShelfVerify" name="inventoryShelfVerify" autofocus="autofocus" value="${whRequest.inventoryShelfVerify}" required/>
                                        </div>
                                    </div>
                                    <div id = "alert_placeholder3"></div>                                        
                                    <br><br>
                                    <div class="pull-right">
                                        <button type="reset" class="btn btn-secondary cancel1">Reset</button>
                                        <button type="submit" name="submit" id="submit" class="btn btn-primary">Save</button>
                                    </div>
                                    <div class="clearfix"></div>
                                </form>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
        <!--END tab menu-->
       
    </s:layout-component>
    <s:layout-component name="page_js">
        <script src="${contextPath}/resources/validation/jquery.validate.min.js"></script>
        <script src="${contextPath}/resources/validation/additional-methods.js"></script>
        <script src="${contextPath}/resources/private/js/bootstrap-datepicker.js"></script>
    </s:layout-component>
    <s:layout-component name="page_js_inline">
        <script>
            $(document).ready(function () {

                
                bootstrap_alert = function () {}
                bootstrap_alert.warning = function (message) {
                    $('#alert_placeholder').html('<div class="alert alert-danger alert-dismissable"><button type="button" class="close" data-dismiss="alert" aria-hidden="true">&times;</button><span>' + message + '</span></div>')
                }
                
                if ($('#barcodeVerify').val() !== "" && $('#barcodeVerify').val() !== $('#materialPassNo').val()) {
                    bootstrap_alert.warning('Barcode Sticker NOT MATCH with Material Pass No! Please re-check and try again.');
                    $("#barcodeVerify").effect("highlight", {}, 1000);
                    $("#barcodeVerify").addClass('highlight');
                }
                
                bootstrap_alert2 = function () {}
                bootstrap_alert2.warning = function (message) {
                    $('#alert_placeholder2').html('<div class="alert alert-danger alert-dismissable"><button type="button" class="close" data-dismiss="alert" aria-hidden="true">&times;</button><span>' + message + '</span></div>')
                }
                if ($('#inventoryRackVerify').val() !== "" && $('#inventoryRackVerify').val() !== $('#inventoryRack').val()) {
                    bootstrap_alert2.warning('The Rack value is NOT MATCH with the inventory\'s record! Please re-check and try again.');
                    $("#inventoryRackVerify").effect("highlight", {}, 1000);
                    $("#inventoryRackVerify").addClass('highlight');
                }
                
                bootstrap_alert3 = function () {}
                bootstrap_alert3.warning = function (message) {
                    $('#alert_placeholder3').html('<div class="alert alert-danger alert-dismissable"><button type="button" class="close" data-dismiss="alert" aria-hidden="true">&times;</button><span>' + message + '</span></div>')
                }
                if ($('#inventoryShelfVerify').val() !== "" && $('#inventoryShelfVerify').val() !== $('#inventoryShelf').val()) {
                    bootstrap_alert3.warning('The Shelf value is NOT MATCH with the inventory\'s record! Please re-check and try again.');
                    $("#inventoryShelfVerify").effect("highlight", {}, 1000);
                    $("#inventoryShelfVerify").addClass('highlight');
                }
                
               
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
                
                var validator = $("#mp_form").validate({
                    rules: {
                        barcodeVerify: {
                            required: true
                        }
                    }
                });
                var validator1 = $("#hi_form").validate({
                    rules: {
                        inventoryRackVerify: {
                            required: true
                        },
                        inventoryShelfVerify: {
                            required: true
                        }
                    }
                });
                
                var element = $('#barcodeVerify');
                if (element.val() === "#materialPassNo") {
                    $("#submit").attr("disabled", true);
                    $("#barcodeVerify").attr("readonly", true);
                }
                
                var element = $('#inventoryRack');
                if (element.val() === "#inventoryRack" && "#materialPassNo" === "#barcodeVerify") {
                    $("#submit").attr("disabled", true);
                    $("#submit1").attr("disabled", true);
                    $("#inventoryRack").attr("readonly", true);
                }
                
                var element = $('#inventoryShelf');
                if (element.val() === "#inventoryShelf" && "#materialPassNo" === "#barcodeVerify") {
                    $("#submit").attr("disabled", true);
                    $("#submit1").attr("disabled", true);
                    $("#inventoryShelf").attr("readonly", true);
                }

                $(".cancel").click(function () {
                    validator.resetForm();
                });
                
                $(".cancel1").click(function () {
                    validator1.resetForm();
                });
            });
        </script>
    </s:layout-component>
</s:layout-render>