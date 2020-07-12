def k8slabel = "jenkins-pipeline-${UUID.randomUUID().toString()}"
def slavePodTemplate = """
      metadata:
        labels:
          k8s-label: ${k8slabel}
        annotations:
          jenkinsjoblabel: ${env.JOB_NAME}-${env.BUILD_NUMBER}
      spec:
        affinity:
          podAntiAffinity:
            requiredDuringSchedulingIgnoredDuringExecution:
            - labelSelector:
                matchExpressions:
                - key: component
                  operator: In
                  values:
                  - jenkins-jenkins-master
              topologyKey: "kubernetes.io/hostname"
        containers:
        - name: terraform
          image: "hashicorp/terraform"
          imagePullPolicy: IfNotPresent
          command:
          - cat
          tty: true
          volumeMounts:
            - mountPath: /var/run/docker.sock
              name: docker-sock
        - name: docker
          image: docker:latest
          imagePullPolicy: IfNotPresent
          command:
          - cat
          tty: true
          volumeMounts:
            - mountPath: /var/run/docker.sock
              name: docker-sock
        - name: packer
          image: hashicorp/packer
          imagePullPolicy: IfNotPresent
          command:
          - cat
          tty: true
          volumeMounts:
            - mountPath: /var/run/docker.sock
              name: docker-sock
        serviceAccountName: default
        securityContext:
          runAsUser: 0
          fsGroup: 0
        volumes:
          - name: docker-sock
            hostPath:
              path: /var/run/docker.sock
    """
    properties([parameters([choice(choices: ['us-east-1', 'us-east-2', 'us-west-1', 'us-west-2'], description: 'Please select a region', name: 'AMI_REGION')])])properties([parameters([choice(choices: ['us-east-1', 'us-east-2', 'us-west-1', 'us-west-2'], description: 'Please select a region', name: 'AMI_REGION')])])
    
    podTemplate(name: k8slabel, label: k8slabel, yaml: slavePodTemplate, showRawYaml: params.DebugMode) {
      node(k8slabel) {
        stage("Build Image") {
            container("packer") {
                sh 'packer --version'
            }
        }
        stage("Send Notification to Slack") {
            container("packer") {
                println("""
                Environment: ${params.Environment}
                """)
                echo 'hello'
            }
        }
        stage("Send Email") {
            container("packer") {
                echo 'hello'
            }
        }
      }
    }