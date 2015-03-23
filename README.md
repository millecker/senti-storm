# *SentiStorm* - Real-time Twitter Sentiment Classification based on Apache Storm

*SentiStorm* is based on [Apache Storm](https://storm.apache.org) and uses different machine learning techniques to identify the sentiment of a tweet. For example, *SentiStorm* uses **Part-of-Speech (POS) tags**, **Term Frequency-Inverse Document Frequency (TF-IDF)** and multiple **sentiment lexica** to extract a feature vector out of a tweet. This extracted feature vector is processed by a **Support Vector Machine (SVM)**, which predicts the sentiment based on a training dataset.

## Topology of *SentiStorm*

<table>
  <tr><td><img src="/docs/images/topology_parallelism.png" alt="Topology of SentiStorm" width="400"></td></tr>
  <tr><td align="center">Topology of <i>SentiStorm</i></td></tr>
</table>


### Tokenizer

### Preprocessor

### POS Tagger

### Feature Vector Generation

### Support Vector Machine (SVM)


## Quality of *SentiStorm*


## Performance of *SentiStorm*


| Nodes | Tokenizer Latency (ms) | Preprocessor Latency (ms) | POS Tagger Latency (ms) | Feature Generation Latency (ms) | SVM Latency (ms) | Complete Latency (ms) | Average Total  Tweets per Second |
|:-----:|:----------------------:|:-------------------------:|:-----------------------:|:-------------------------------:|:----------------:|:---------------------:|:--------------------------------:|
| 1 | 0.179 | 0.108 | 1.492 | 0.185 | 0.953 | 48.155 | 3133 |
| 2 | 0.182 | 0.108 | 1.514 | 0.183 | 0.987 | 51.048 | 5920 |
| 3 | 0.189 | 0.112 | 1.531 | 0.183 | 1.034 | 52.607 | 8599 |
| 4 | 0.187 | 0.109 | 1.543 | 0.180 | 1.023 | 52.311 | 11528 |
| 5 | 0.188 | 0.110 | 1.536 | 0.183 | 1.023 | 52.657 | 14295 |
| 6 | 0.184 | 0.108 | 1.532 | 0.179 | 1.025 | 53.332 | 17025 |
| 7 | 0.182 | 0.109 | 1.544 | 0.178 | 1.022 | 53.575 | 19735 |
| 8 | 0.187 | 0.110 | 1.549 | 0.178 | 1.025 | 53.359 | 22576 |
| 9 | 0.180 | 0.107 | 1.521 | 0.177 | 1.016 | 54.055 | 25207 |
| 10 | 0.182 | 0.107 | 1.528 | 0.176 | 1.031 | 53.889 | 27876 |


<table>
  <tr><td><img src="/docs/images/performance_storm_c3.8xlarge.png" alt="SentiStorm Performance" width="500" /></td></tr>
  <tr><td align="center">Throughput of <i>SentiStorm</i> based on the SemEval 2013<br> dataset and <i>c3.8xlarge</i> EC2 nodes</td></tr>
</table>

## References


