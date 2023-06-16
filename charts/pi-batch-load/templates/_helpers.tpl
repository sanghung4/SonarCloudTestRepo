{{/*
Expand the name of the chart.
*/}}
{{- define "pi-batch-load.name" -}}
{{- default .Chart.Name .Values.nameOverride | trunc 63 | trimSuffix "-" -}}
{{- end -}}

{{/*
Create a default fully qualified app name.
We truncate at 63 chars because some Kubernetes name fields are limited to this (by the DNS naming spec).
If release name contains chart name it will be used as a full name.
*/}}
{{- define "pi-batch-load.fullname" -}}
{{- if .Values.fullnameOverride }}
{{- .Values.fullnameOverride | trunc 63 | trimSuffix "-" }}
{{- else }}
{{- $name := default .Chart.Name .Values.nameOverride }}
{{- if contains $name .Release.Name }}
{{- .Release.Name | trunc 63 | trimSuffix "-" }}
{{- else }}
{{- printf "%s-%s" .Release.Name $name | trunc 63 | trimSuffix "-" }}
{{- end }}
{{- end }}
{{- end }}

{{/*
Create chart name and version as used by the chart label.
*/}}
{{- define "pi-batch-load.chart" -}}
{{- printf "%s-%s" .Chart.Name .Chart.Version | replace "+" "_" | trunc 63 | trimSuffix "-" }}
{{- end }}

{{/*
Common labels
*/}}
{{- define "pi-batch-load.labels" -}}
helm.sh/chart: {{ include "pi-batch-load.chart" . }}
{{ include "pi-batch-load.selectorLabels" . }}
{{- if .Chart.AppVersion }}
app.kubernetes.io/version: {{ .Chart.AppVersion | quote }}
{{- end }}
app.kubernetes.io/managed-by: {{ .Release.Service }}
{{- end }}


{{/*
Selector labels
*/}}
{{- define "pi-batch-load.selectorLabels" -}}
app.kubernetes.io/name: {{ include "pi-batch-load.name" . }}
app.kubernetes.io/instance: {{ .Release.Name }}
worker-group: fargate
{{- end }}

{{/*
Create the name of the service account to use
*/}}
{{- define "pi-batch-load.serviceAccountName" -}}
{{- if .Values.serviceAccount.create }}
{{- default (include "pi-batch-load.fullname" .) .Values.serviceAccount.name }}
{{- else }}
{{- default "default" .Values.serviceAccount.name }}
{{- end }}
{{- end }}
