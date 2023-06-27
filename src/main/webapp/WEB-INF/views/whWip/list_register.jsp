<%-- 
    Document   : list_register
    Created on : Jun 21, 2023, 5:09:06 PM
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
            <h1>WIP Management - Register WIP</h1>
            <div class="row">
                <div class="col-lg-8">
                    <div class="main-box">
                        <h2>Scan Trip Ticket / RMS Event</h2>
                        <form id="add_mp_list_form" class="form-horizontal" role="form" action="${contextPath}/whWip/updateVerifyToUpdate" method="post">
                            <div class="form-group">
                                <label for="boxNo" class="col-lg-3 control-label">Box Number *</label>
                                <div class="col-lg-8">
                                    <input type="text" class="form-control" id="boxNo" name="boxNo" placeholder="" value="" autofocus="autofocus">
                                </div>
                            </div>
                            <a href="${contextPath}/wh/whRequest/ship" class="btn btn-info pull-left"><i class="fa fa-reply"></i> Back</a>
                            <div class="pull-right">
                                <button type="reset" class="btn btn-secondary cancel">Reset</button>
                                <button type="submit" class="btn btn-primary">Add</button>
                            </div>
                            <div class="clearfix"></div>
                        </form>
                    </div>
                </div>	
            </div>
        </div>
        <div class="col-lg-12">
            <h1>WIP Management - Register WIP</h1>
            <div class="row">
                <div class="col-lg-8">
                    <div class="main-box clearfix">
                            <h2 class="pull-left">Scan Trip Ticket / RMS Event</h2>
                            <div class="filter-block pull-right">
                                <a href="${contextPath}/whWip/registerPage" data-toggle="modal" class="btn btn-primary pull-right">
                                    <i class="fa fa-bars fa-lg"></i> button Register WIP
                                </a>
                            </div>
                            <div class="filter-block pull-right">
                            </div>
                        <hr/>
                        <div class="clearfix">
                            <div class="form-group pull-left">
                                <select id="dt_spml_rows" class="form-control">
                                    <option value="10">10</option>
                                    <option value="25">25</option>
                                    <option value="50">50</option>
                                    <option value="100">100</option>
                                </select>
                            </div>
                            <div class="filter-block pull-right">
                                <div id="dt_spml_tt" class="form-group pull-left" style="margin-right: 5px;">
                                </div>
                                <div class="form-group pull-left" style="margin-right: 0px;">
                                    <input id="dt_spml_search" type="text" class="form-control" placeholder="<f:message key="general.label.search"/>">
                                    <i class="fa fa-search search-icon"></i>
                                </div>
                            </div>
                        </div>
                        <div class="table-responsive">
                            <table id="dt_spml" class="table">
                                <thead>
                                    <tr>
                                        <th><span>No</span></th>
                                        <th><span>Request ID</span></th> 
                                        <th><span>GTS NO</span></th>
                                        <th><span>RMS Event</span></th>
                                        <th><span>Intervals</span></th>
                                        <th><span>Quantity</span></th>
                                        <th><span>Shipment Date</span></th>
                                        <th><span>Status</span></th>
                                    </tr>
                                </thead>
                                <tbody>
                                    <c:forEach items="${wipList}" var="whWip" varStatus="whWipLoop">
                                        <tr>
                                            <td><c:out value="${whWipLoop.index+1}"/></td>
                                            <td><c:out value="${whWip.requestId}"/></td>
                                            <td><c:out value="${whWip.gtsNo}"/></td>
                                            <td><c:out value="${whWip.rmsEvent}"/></td>
                                            <td><c:out value="${whWip.intervals}"/></td>
                                            <td><c:out value="${whWip.quantity}"/></td>
                                            <td><c:out value="${whWip.shipmentDate}"/></td>
                                            <td><c:out value="${whWip.status}"/></td>
                                        </tr>
                                    </c:forEach>
                                </tbody>
                            </table>
                        </div>
                    </div>
                </div>
            </div>
        </div>
        <hr class="separator">
    </s:layout-component>
    <s:layout-component name="page_js">
        <!--<script src="${contextPath}/resources/private/datatables/js/buttons.html5.min.js"></script>-->
    </s:layout-component>
    <s:layout-component name="page_js_inline">
        <script>
        </script>
    </s:layout-component>
</s:layout-render>