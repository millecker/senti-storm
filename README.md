# *SentiStorm* - Real-time Twitter Sentiment Classification based on Apache Storm

*SentiStorm* is based on [Apache Storm](https://storm.apache.org) and uses different machine learning techniques to identify the sentiment of a tweet. For example, *SentiStorm* uses Part-of-Speech (POS) tags, Term Frequency-Inverse Document Frequency (TF-IDF) and multiple sentiment lexica to extract a feature vector out of a tweet. This extracted feature vector is processed by a Support Vector Machine (SVM), which predicts the sentiment based on a training dataset.

## Topology of *SentiStorm*
<img src="/docs/images/topology_parallelism.png" alt="Topology of SentiStorm" width="500" />


## Quality Evaluation of *SentiStorm*


## Performance Evaluation of *SentiStorm*
<img src="/docs/images/performance_storm_c3.8xlarge.png" alt="SentiStorm Performance" width="500" />


## References


