<%
    ui.decorateWith("appui", "standardEmrPage")
%>

<style>
    body {
        width: 99%;
        max-width: none;
    }
    table td {
        border: none;
    }
    table {
        border-collapse: separate;
    }
    td {
        vertical-align: top;
        background-color: white;
    }
    .question {
        text-align: right;
        white-space: nowrap;
    }
    .value {
        text-align: left;
        white-space: nowrap;
    }
    #form-actions {
        float: right;
    }
    .form-action-link {
        padding-left: 10px; padding-right:10px;
    }
    #weight-graph {
        width:500px; height:250px;
    }
    .yaxis-label {
        vertical-align: middle;
        -webkit-transform: rotate(270deg);
        -moz-transform: rotate(270deg);
        -ms-transform: rotate(270deg);
        -o-transform: rotate(270deg);
        transform: rotate(270deg);
    }
    .nowrap {
        white-space: nowrap;
    }
    .header-section {
        width:100%; padding:10px;
    }
    .section-divider-top {
        border-bottom: 2px solid black; padding-bottom:10px;
    }
    .section-divider-bottom {
        padding-top:10px;
    }
    .first-column {
        padding:0px 10px 0px 10px; white-space:nowrap; vertical-align:top;
    }
    .second-column {
        padding:0px 75px 0px 25px; white-space:nowrap; vertical-align:top;
    }
    .third-column {
        padding:0px 10px 0px 10px; white-space:nowrap; vertical-align:top; width:100%;
    }
    .detail-table td {
        padding: 0px 20px 0px 0px;
    }
    #weightsTable {
        white-space: nowrap;
        float: left;
        width: 350px
    }
    #weightsTable thead {
        display: block
    }
    #weightsTable thead th {
        background: #f3f3f3;
        text-align: left
    }
    #weightsTable tbody {
        display: block;
        height: 262px;
        overflow: auto;
        width: 100%
    }
    #weightsTable tbody td {
        background: #FFF;
        border: 1px solid #f3f3f3;
    }
    #weightsTable thead th {
        width: 175px
    }
    #weightsTable thead th + th {
        width: 137px
    }
    #weightsTable tbody td {
        width: 175px
    }
    #weightsTable tbody td + td {
        width: 191px
    }
    .alert {
        font-weight:bold;
        color:red;
    }
</style>

<style type="text/css" media="print">
    @page {
        size: portrait;
        margin:.2in;
    }
    .hide-when-printing {
        display: none;
    }
    body {
        font-size: 1em;
    }
    .header-section {
        padding:20px;
    }
    .section-divider-top {
        padding-bottom:20px;
    }
    .section-divider-bottom {
        padding-top:20px;
    }
</style>

<div id="printable-summary">

    <div id="form-actions" class="hide-when-printing">
        <a class="form-action-link" id="back-link" href="/${ ui.contextPath() }/patientDashboard.form?patientId=${ patient.id }">
            <i class="icon-chevron-left"></i>
            Return to Patient Dashboard
        </a>
        <a class="form-action-link" id="print-form-link">
            <i class="icon-print"></i>
            ${ ui.message("uicommons.print") }
        </a>
    </div>

    <div class="header-section">
        <span id="name-section" style="font-size:2em;">${ firstName } ${ lastName }</span>
    </div>

    This is a printable patient summary example.  We would add more data here...

</div>
