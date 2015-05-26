[@ww.select labelKey="agent.capability.type.capability.go.key" name="goExecutableKind"
  list=capabilityType.executableTypes listKey="key" listValue="value" toggle=true /]
[#list capabilityType.executableTypes.keySet() as executableTypeKey]
  [@ui.bambooSection dependsOn="goExecutableKind" showOn=executableTypeKey]
    [@ww.textfield labelKey="agent.capability.type.capability.go.value" name=executableTypeKey value=""
      description=capabilityType.getExecutableDescription(executableTypeKey) cssClass="long-field" /]
  [/@ui.bambooSection]
[/#list]
