# AgileSoftwareModel

This software models the performance of an agile software development team in a fully autonomous task allocation environment. The team is currently comprised of six software developers and one tester. However, the nature of tasks that are defined for a project currently exclude any testing; therefore the tester is practically inactive throughout the modelling process. 

Tasks are defined based on three skill areas of "Back End, Front End and Design", and the six developers select tasks based on their capabilities (called experties in the model) in each skill area. The task allocation strategies are based on two different approaches of "Expertise Based" and "Learning Based". In the first approach, a developer gives higher priority to tasks that match his/her better skill, and in the latter a developer gives priority to a task is more focused on the less enhanced expertise of that developer.

The system has a predefined backlog of randomly generated tasks, and has generated ten random permutations of the backlog. The benchmarks are run based on these permutations, each of which is run with ExpertiseBased and LearningBased strategies. The system also defines six different experimental setups, each of which defines initial expertise levels for team members to mimic different combination of expertise within a team.

The structure of the project presented in this repository is as follows:

## Defaults:

This directory contains the default settings for the fundamental elements of the simulation.

## Files:

This directory contains the information about the fundamental settings of the simulation, that is used by the system everytime it strats up. The system allows roling back to the default settings, in which case the content of this folder is set back to the content of the "Defaults" directory. Therefore, the "Defaults" directory should remain unchanged. 

## src:

This directory contains the source code of the modelling software. 

## Statistics:

This directory contains the results of the latest benchmark runs. Note that the older records are kept in another branch called "OldResults". For performing sampling operations on the acquired results, please refer to the tool provided in the "FileProcessor" branch. The implementation requires some manual modifications to tailor the tool for customized samplings. 

# Running the model

For running the model, simply run the "Main" class either from an IDE such as Eclipse, or using the command line console.
