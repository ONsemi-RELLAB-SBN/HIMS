<%-- 
    Document   : list_verify
    Created on : Jun 21, 2023, 5:08:59 PM
    Author     : zbqb9x
--%>

<%@page contentType="text/html;charset=UTF-8" %>
<%@include file="/WEB-INF/base/taglibs.jsp" %>
<s:layout-render name="/WEB-INF/base/base.jsp">
    <s:layout-component name="page_css">
        <link rel="stylesheet" href="${contextPath}/resources/private/datatables/css/jquery.dataTables.css" type="text/css" />
        <link rel="stylesheet" href="${contextPath}/resources/private/datatables/css/buttons.dataTables.min.css" type="text/css" />
    </s:layout-component>
    <s:layout-component name="page_css_inline">
        <style>
            @media print {
                table thead {
                    border-top: #000 solid 2px;
                    border-bottom: #000 solid 2px;
                }
                table tbody {
                    border-top: #000 solid 2px;
                    border-bottom: #000 solid 2px;
                }
            }
            .dataTables_wrapper .dt-buttons {
                float:none;  
                text-align:right;
            }
        </style>
    </s:layout-component>
    <s:layout-component name="page_container">
        <div class="col-lg-12">
            <h1>WIP Management</h1>
            <div class="row">
                <div class="col-lg-8">
                    <div class="main-box">
                        <h2>WIP Information - Verify WIP</h2>
                        <form id="wip_information_form" class="form-horizontal" role="form" action="${contextPath}/whWip/updateReceiveToVerify/${wipData.requestId}" method="post">
                            <input type="hidden" name="requestId" value="${wipData.requestId}" />
                            <div class="form-group">
                                <label for="gtsNo" class="col-lg-3 control-label">GTS No.</label>
                                <div class="col-lg-8">
                                    <input type="text" class="form-control" id="gtsNo" name="gtsNo" value="${wipData.gtsNo}" readonly>
                                </div>
                            </div>
                            <div class="form-group">
                                <label for="rms" class="col-lg-3 control-label">RMS Event No.</label>
                                <div class="col-lg-8">
                                    <input type="text" class="form-control" id="rms" name="rms" value="${wipData.rmsEvent}" readonly>
                                </div>
                            </div>
                            <div class="form-group">
                                <label for="intervals" class="col-lg-3 control-label">Intervals</label>
                                <div class="col-lg-8">
                                    <input type="text" class="form-control" id="intervals" name="intervals" value="${wipData.intervals}" readonly>
                                </div>
                            </div>
                            <div class="form-group">
                                <label for="quantity" class="col-lg-3 control-label">Quantity</label>
                                <div class="col-lg-8">
                                    <input type="text" class="form-control" id="quantity" name="quantity" value="${wipData.quantity}" readonly>
                                </div>
                            </div>
                            <div class="form-group">
                                <label for="shipDate" class="col-lg-3 control-label">Shipment Date</label>
                                <div class="col-lg-8">
                                    <input type="text" class="form-control" id="shipDate" name="shipDate" value="${wipData.shipmentDate}" readonly>
                                </div>
                            </div>
                            <div id = "alert_placeholder2"></div>
                            <!--<a href="${contextPath}/whWip/listNew" class="btn btn-info pull-left"><i class="fa fa-reply"></i> Back</a>-->
                            <a href="${contextPath}/whWip/listNew" class="btn btn-info pull-left"><i class='bx bxs-chevron-left bx-fw'></i> Back</a>
                            <div class="clearfix"></div>
                        </form>
                    </div>
                </div>
            </div>
                            
            <div class="row">
                <div class="col-lg-8">
                    <div class="main-box">
                        <h2>Scan Trip Ticket</h2>
                        <form id="wip_verification_form" class="form-horizontal" role="form" action="${contextPath}/whWip/updateVerify" method="post">
                            <input type="hidden" name="requestId" value="${wipData.requestId}" />
                            <input type="hidden" id="rmsEvent" value="${wipData.rmsEvent}" />
                            <div class="form-group">
                                <label for="boxNo" class="col-lg-3 control-label">Scan Trip Ticket *</label>
                                <div class="col-lg-8">
                                    <input type="text" class="form-control" id="tripTicket" name="tripTicket" placeholder="" value="" autofocus="autofocus">
                                </div>
                            </div>
                            <div class="pull-right">
                                <button type="submit" id="verify" class="btn btn-primary"><i class='bx bx-log-in-circle bx-md bx-fw' style='color:#ffffff' ></i> Verify</button>
                            </div>
                            <div class="clearfix"></div>
                        </form>
                    </div>
                </div>	
            </div>
        </div>
        <hr class="separator">
    </s:layout-component>
    <s:layout-component name="page_js">
        <script src="${contextPath}/resources/validation/jquery.validate.min.js"></script>
        <script src="${contextPath}/resources/validation/additional-methods.js"></script>
        <script src="${contextPath}/resources/private/js/bootstrap-datepicker.js"></script>
        <script src="${contextPath}/resources/private/datatables/js/jquery.dataTables.min.js"></script>
        <script src="${contextPath}/resources/private/datatables/js/dataTables.buttons.min.js"></script>
        <script src="${contextPath}/resources/private/datatables/js/buttons.print.min.js"></script>
        <script src="${contextPath}/resources/private/datatables/js/buttons.flash.min.js"></script>
        <script src="${contextPath}/resources/private/datatables/js/buttons.html5.min.js"></script>
    </s:layout-component>
    <s:layout-component name="page_js_inline">
        <script>
            $(document).ready(function () {
                // CODE TO DISABLE THE FEATURE
//                $('#inventoryRack').bind('copy paste cut', function (e) {
//                    e.preventDefault();
//                });

                jQuery.extend(jQuery.validator.messages, {
                    required: "This field is required.",
                    equalTo: "Value is not match! Please re-scan.",
                    email: "Please enter a valid email.",
                });

                $(".cancel").click(function () {
                    validator.resetForm();
                });
                
                var validator = $("#wip_verification_form").validate({
                    rules: {
                        tripTicket: {
                            required: true,
                            equalTo: "#rmsEvent"
                        }
                    }
                });
            });
        </script>
    </s:layout-component>
</s:layout-render>