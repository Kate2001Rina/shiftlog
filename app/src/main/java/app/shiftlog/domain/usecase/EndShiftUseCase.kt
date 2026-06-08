package app.shiftlog.domain.usecase

import app.shiftlog.domain.model.Shift
import app.shiftlog.domain.repository.ShiftRepository
import javax.inject.Inject

class EndShiftUseCase @Inject constructor(
    private val repository: ShiftRepository
) {
    suspend operator fun invoke(shiftId: Long): Result<Shift> = runCatching {
        repository.endShift(shiftId)
    }
}