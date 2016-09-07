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
                <div class="col-lg-6">
                    <div class="main-box">
                        <h2>Details</h2>
                        <form id="approval_hardwarequest_form" class="form-horizontal" role="form" action="${contextPath}/wh/whRequest/approvalupdate" method="post">
                            <input type="hidden" name="refId" value="${whRequest.refId}" />
                            <div class="form-group">
                                <label for="equipmentType" class="col-lg-4 control-label">Hardware Category</label>
                                <div class="col-lg-8">
                                    <input type="text" class="form-control" id="equipmentType" name="equipmentType" value="${whRequest.equipmentType}" readonly>
                                </div>
                            </div>
                            <div class="form-group">
                                <label for="equipmentId" class="col-lg-4 control-label">Equipment ID</label>
                                <div class="col-lg-8">
                                    <input type="text" class="form-control" id="equipmentId" name="equipmentId" value="${whRequest.equipmentId}" readonly>
                                </div>
                            </div>  
                            <div class="form-group" id="quantitydiv" hidden>
                                <label for="quantity" class="col-lg-4 control-label">Quantity *</label>
                                <div class="col-lg-2">
                                    <input type="text" class="form-control" id="quantity" name="quantity" placeholder="Quantity" value="${whRequest.quantity}" readonly>
                                </div>
                            </div>
                            <div class="form-group">
                                <label for="materialPassNo" class="col-lg-4 control-label">Material Pass No.</label>
                                <div class="col-lg-8">
                                    <input type="text" class="form-control" id="materialPassNo" name="materialPassNo" value="${whRequest.materialPassNo}" readonly>
                                </div>
                            </div>
                            <div class="form-group">
                                <label for="materialPassExpiry" class="col-lg-4 control-label">Material Pass Expiry Date</label>
                                <div class="col-lg-8">
                                    <input type="text" class="form-control" id="materialPassExpiry" name="materialPassExpiry" value="${whRequest.materialPassExpiry}" readonly>
                                </div>
                            </div>
                            <div class="form-group">
                                <label for="inventoryLoc" class="col-lg-4 control-label">Inventory Location</label>
                                <div class="col-lg-8">
                                    <input type="text" class="form-control" id="inventoryLoc" name="inventoryLoc" value="${whRequest.inventoryLoc}" readonly>
                                </div>
                            </div>  
                            <div class="form-group">
                                <label for="requestedBy" class="col-lg-4 control-label">Requested By</label>
                                <div class="col-lg-8">
                                    <input type="text" class="form-control" id="requestedBy" name="requestedBy" placeholder="Request For" value="${whRequest.requestedBy}" readonly>
                                </div>
                            </div>
                            <div class="form-group">
                                <label for="requestedEmail" class="col-lg-4 control-label">Requestor Email</label>
                                <div class="col-lg-8">
                                    <input type="text" class="form-control" id="requestedEmail" name="requestedEmail" placeholder="requestedEmail" value="${whRequest.requestedEmail}" readonly>
                                </div>
                            </div>
                            <div class="form-group">
                                <label for="requestedDate" class="col-lg-4 control-label">Requested Date</label>
                                <div class="col-lg-8">
                                    <input type="text" class="form-control" id="requestedDate" name="requestedDate" placeholder="Request For" value="${whRequest.requestedDate}" readonly>
                                </div>
                            </div>
                            
                            <div class="form-group">
                                <label for="remarks" class="col-lg-4 control-label">Remarks </label>
                                <div class="col-lg-8">
                                    <textarea class="form-control" rows="5" id="remarks" name="remarks" readonly>${whRequest.remarks}</textarea>
                                </div>
                            </div>
                            <div class="form-group">
                                <label for="status" class="col-lg-4 control-label">Status</label>
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
                </ul>
                <div class="tab-content">

                    <!--Tab for Scan Material Pass-->
                    <div id="mp" class="tab-pane fade ${mpActiveTab}">

                        <h6></h6>
                        <div class="col-lg-6">
                            <div class="main-box">
                                <h2>Verify Barcode Label</h2>
                                <form id="mp_form" class="form-horizontal" role="form" action="${contextPath}/wh/whRequest/setShipping" method="post">
                                    <input type="hidden" name="refId" value="${whRequest.refId}" />
                                    <input type="hidden" name="status" value="${whRequest.status}" />
                                    <input type="hidden" id="materialPassNo" name="materialPassNo" value="${whRequest.materialPassNo}" />
                                    <input type="hidden" name="flag" value="${whRequest.flag}" />
                                    <input type="hidden" name="materialPassExpiry" value="${whRequest.materialPassExpiry}" />
                                    <!--input type="hidden" name="refId" value="${mpActiveTab}" /-->
                                    <div class="form-group">
                                        <label for=" barcodeVerify" class="col-lg-4 control-label">Scan Barcode Label</label>
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
                    //                    $("#hardwareBarcode2").effect("highlight", {}, 1000);
                    $("#hardwareBarcode2").addClass('highlight');
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
                            required: true,
                            equalToMpNo: materialPassNo
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