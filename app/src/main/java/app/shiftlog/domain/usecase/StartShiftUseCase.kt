package app.shiftlog.domain.usecase

import app.shiftlog.domain.model.Shift
import app.shiftlog.domain.repository.ShiftRepository
import javax.inject.Inject

class StartShiftUseCase @Inject constructor(
    private val repository: ShiftRepository
) {
    // нельзя начать смену если другая ещё активна
    suspend operator fun invoke(): Result<Shift> {
        val active = repository.getActiveShift()
        if (active != null) {
            return Result.failure(IllegalStateException("Shift already active"))
        }
        return Result.success(repository.startShift())
    }
}